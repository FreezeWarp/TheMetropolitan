<?php
define('SECRET_KEY', 'J8Z30QqSq9pUrJojnA4X1JpEnh9F1sukxIffKeRqo3Z6eygR9RUq1nEyx3HI');
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);


class Author {
    public $authorID;

    /* Author Properties */
    protected $authorName;
    protected $email;
    protected $authorType;
    protected $facebook;
    protected $twitter;
    protected $linkedin;
    protected $website;
    protected $blurb;

    /* Class Information */
    private $database;
    private $databaseFetched = false;
    private $databaseFetchedSuccess = false;


    /**
     * Author constructor.
     * @param database $database
     * @param int $authorID
     * @param string $authorName
     */
    function __construct($database, $authorID, $authorName = false) {
        $this->authorID = $authorID;
        $this->authorName = $authorName;
        $this->database = $database;
    }


    /**
     * Returns true when the object can be used to obtain all author data. Returns false if only partial data is present. Does not check if the author actually exists in the database.
     * @internal When authorID is present, we can query for all remaining information. However, the object can be created with just authorName, and we currently don't enable querying from authorName.
     * @return bool
     */
    function isValid() {
        return $this->authorID;
    }


    /**
     * DEPRECATED
     */
    public function id() {
        return $this->authorID;
    }


    private function databaseFetch() {
        if ($this->database) {
            foreach ($this->database->select([
                'new_authors' => 'authorID, authorName, email, authorType, facebook, twitter, linkedin, website, blurb',
            ], ['authorID' => $this->authorID])->getAsArray(false) AS $attribute => $value) {
                $this->$attribute = $value;
            }
        }
    }

    public function __get($param) {
        if (!$this->$param && !$this->databaseFetched) $this->databaseFetch(); // By default, the class will be loaded with an ID and hopefully a name. However, if a requested attribute is falsey, and we haven't yet checked the database, we should fetch it. (Naturally, the database can return falsey data, so we don't want to requery the database.)

        return $this->$param;
    }

    /**
     * DEPRECATED
     */
    public function get($param) {
        return $this->__get($param);
    }


    public function getHeadshotDirectory() {
        return '/headshots/' . $this->authorID . '.jpg';
    }


    public function getAuthorName() {
        return (ctype_upper($this->__get('authorName'))
            ? ucwords(strtolower(trim($this->__get('authorName'))), " \t\r\n\f\v.")
            : trim($this->__get('authorName'), " \t\r\n\f\v.")
            );
    }


    public function getAuthorPath() {
        return '/author/' . $this->authorID . '/';
    }


    public function getAuthorPageHeader() {
        $titleList = '<div class="authorBig">' . (file_exists('headshots/' . $this->authorID . '.jpg') ? '<img src="' . $this->getHeadshotDirectory() . '" class="headshot" /><br /><header class="headshotCover"><div>' : '<header><div>') . '<h1>' . $this->getAuthorName() . '</h1><ul class="authorSocialMedia">';

        foreach ([
                     "twitter" => ["https://twitter.com/", "Twitter"],
                     "facebook" => ["https://www.facebook.com/", "Facebook"],
                     "linkedin" => ["https://www.linkedin.com/in/", "LinkedIn"],
                     "email" => ["mailto:", "Email"],
                     "website" => ["", "Website"]
                 ] AS $service => $options) {
            if ($this->$service) $titleList .= '<li><span class="imageIcon ' . $service . 'Icon"></span><a name="' . $service . 'Link" href="' . $options[0] . $this->__get($service) . '" data-sourceValue="' .  $this->__get($service) . '" target="_blank">' . $options[1] . '</a></li>';
        }

        $titleList .= '</ul></div></header>';

        $titleList .= '</div>';

        return $titleList;
    }
}

class IssueID {
    public $year;
    public $month;

    const YEAR_FORMAT = '%04d';
    const MONTH_FORMAT = '%02d';
    const ISSUE_STRING_FORMAT = '%04d-%02d-01'; // We could concatenate these if PHP 5.6

    static function parseIssueString($issueString) {
        return sscanf($issueString, self::ISSUE_STRING_FORMAT);
    }
    static function yearFromIssueString($issueString) {
        return self::parseIssueString($issueString)[0];
    }
    static function monthFromIssueString($issueString) {
        return self::parseIssueString($issueString)[1];
    }

    public function __construct($year, $month) {
        $this->year = (int) $year;
        $this->month = (int) $month;
    }

    public function isValid() {
        return ($this->year && $this->month);
    }

    public function getYear() {
        return $this->year;
    }

    public function getMonth() {
        return $this->month;
    }

    public function __toString() {
        return $this->getIssueString();
    }

    public function getIssueString() {
        return sprintf(self::ISSUE_STRING_FORMAT, $this->year, $this->month);
    }

    public function getYearString() {
        return sprintf(self::YEAR_FORMAT, $this->year);
    }

    public function getMonthString() {
        return sprintf(self::MONTH_FORMAT, $this->month);
    }

    public function getIssueName() {
        return date('F, Y', strtotime($this->year . '-' . $this->month . '-1'));
    }

    public function getIssuePath() {
        return 'issue/' . $this->getYearString() . '/' . $this->getMonthString() . '/';
    }

    public function getIssueURL() {
        return '/' . $this->getIssuePath();
    }

    public function getArticleURLWithNum($articleNum) {
        return (new ArticleID($this->year, $this->month, $articleNum))->getArticleURL();
    }

    public function fetchTitleCache(database $database) {
        $issue = $database->select([
            'new_issues' => 'issue, titleCache'
        ], [
            'issue' => $this->getIssueString(),
        ])->getAsArray(false);

        if (empty($issue))
            return false;

        return $issue['titleCache'];
    }

    public function refreshTitleCache(database $database) {
        $articles = new ArticleBuilderDatabase($database, ['issueID' => $this]);

        // Build the HTML tree from $articles
        $header = '<h1 id="inthisissue">In Our <a href="' . $articles->getParam('issueID')->getIssueURL() . '">' . $articles->getParam('issueID')->getIssueName() . ' Issue</a>:</h1>
            <ul class="titlelist">';

        foreach ($articles AS $article) {
            $header .= '<li>
                    <a href="' . $article->getArticleURL() . '">
                        <span class="title">' . $article->title . '</span><span class="titleAuthorSeparator"> by </span><span class="author">' . $article->getAuthorString() . '</span>
                    </a>
                </li>';
        }

        $header .= '</ul><a id="articles"></a>';

        $database->update('new_issues', [
            'titleCache' => $header,
        ], [
            'issue' => $this->getIssueString()
        ]);
    }
}

class ArticleID extends IssueID {
    public $articleNum;

    const ARTICLENUM_FORMAT = '%03d';
    const ARTICLE_FILENAME_FORMAT = "%04d-%02d-01-%03d.html"; // Concatenate with ISSUE_STRING_FORMAT in PHP 5.6

    public function __construct($year, $month, $articleNum) {
        parent::__construct($year, $month);

        $this->articleNum = $articleNum;
    }

    public function getIssueIDCopy() {
        return new IssueID($this->year, $this->month);
    }

    static function parseArticleString($issueString) {
        return sscanf($issueString, self::ARTICLE_FILENAME_FORMAT);
    }
    static function yearFromArticleString($issueString) {
        return self::parseArticleString($issueString)[0];
    }
    static function monthFromArticleString($issueString) {
        return self::parseArticleString($issueString)[1];
    }
    static function articleNumFromArticleString($issueString) {
        return self::parseArticleString($issueString)[2];
    }

    public function isValid() {
        return (parent::isValid() && $this->articleNum >= 0);
    }

    public function __toString() {
        return $this->getArticleString();
    }

    public function getArticleString() {
        return parent::getIssueString() . '-' . $this->getArticleNumString();
    }

    public function getArticleNumString() {
        return sprintf($this::ARTICLENUM_FORMAT, $this->articleNum);
    }

    public function getArticleURL() {
        return parent::getIssueURL() . $this->getArticleNumString() . '/';
    }

    public function getArticlePath() {
        return parent::getIssuePath() . sprintf(self::ARTICLE_FILENAME_FORMAT, $this->year, $this->month, $this->articleNum);
    }
}

class Article extends ArticleID {
    //protected $articleData;

    public $absoluteLink;
    public $authors;
    public $content;
    public $excerpt;
    public $title;
    public $displayOptions;

    public function __construct($year, $month, $articleNum, $articleData) {
        parent::__construct($year, $month, $articleNum);

        $this->absoluteLink = $this->getIssueURL() . $this->getArticleNumString() . '/';
        $this->authors = $articleData['authors'];
        $this->content = $articleData['content'];
        $this->excerpt = $articleData['excerpt'];
        $this->title = $articleData['title'];
        $this->displayOptions = (isset($articleData['displayOptions']) ? $articleData['displayOptions'] : 0);
    }

    public function getAuthorString() {
        $authorNames = [];

        foreach ($this->authors AS $author) {
            $authorNames[] = $author->getAuthorName();
        }

        return implode(',', $authorNames);
    }

    public function getAuthorStringAnchored() {
        $authorNames = [];

        foreach ($this->authors AS $author) {
            $authorNames[] = '<a href="' . $author->getAuthorPath() . '" rel="author">' . $author->getAuthorName() . '</a>';
        }

        return implode(',', $authorNames);
    }

    public function get($param) {
        return $this->$param;
    }
}

function cache($output) {
    echo 'Caching...';
    global $cachePath;

    if (!is_dir(dirname($cachePath)))
        mkdir(dirname($cachePath), 0755, true);

    file_put_contents($cachePath, $output);

    return $output;
}

function isAdmin() {
    return isset($_REQUEST['secretkey'])
    && $_REQUEST['secretkey'] === SECRET_KEY;
}

$mode = 'database'; // import, html, or database
if (isAdmin()
    && isset($_GET['mode'])) $mode = $_GET['mode'];
?>
