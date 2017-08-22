<?php
define('EXCERPT_BONUS_PER_ITEM', 100);
define('EXCERPT_BONUS_PER_SLIDER', 850);
define('EXCERPT_FLEX', 250);
define('EXCERPT_THRESHOLD', 2500);

define('DISPLAY_FORCE_RIGHT', 1);
define('DISPLAY_FORCE_LEFT', 2);
define('DISPLAY_FORCE_TOP', DISPLAY_FORCE_LEFT+DISPLAY_FORCE_RIGHT);
define('DISPLAY_PREFER_TOP', 4);

mb_internal_encoding("UTF-8");
libxml_use_internal_errors(true);

require_once('functions.php');

if ($mode === 'database' || $mode === 'import') {
  require('dba/database.php');
  require('dba/databaseSQL.php');

  $database = new databaseSQL;

  /* For testing. */
  $database->printErrors = true;
  $database->setErrorLevel(E_USER_NOTICE);

  /* Connect */
  if (!$database->connect('localhost', 3306, 'xxxx', 'xxx', 'xxxx', 'mysqli', ''))
      $mode = 'html';
}

function listArray($array) {
  $length = count($array);
  $stringBuilder = '';

  foreach ($array AS $element) {
    $length--;

    if ($length > 1) {
      $stringBuilder .= ($element . ', ');
    }
    elseif ($length === 1) {
      $stringBuilder .= ($element . ' and ');
    }
    elseif ($length === 0) {
      $stringBuilder .= $element;
    }
    else {
      die('Logic error.');
    }
  }

  return $stringBuilder;
}

function buildImageDataArray($filePath) {
    if ($filePath) {
        $file_headers = @get_headers($filePath);

        if($file_headers[0] !== 'HTTP/1.1 404 Not Found') {
            list($width, $height) = getimagesize($filePath);

            return [
                'path' => $filePath,
                'height' => $height,
                'width' => $width,
            ];
        }
    }

    return false;
}

abstract class ArticleBuilder implements Iterator {
    public $articles = []; // Should be filled on construction by Article implementator
    protected $articlesPosition = 0;
    protected $params = [];

    const DISPLAY_MODE_AUTHOR = 'author';
    const DISPLAY_MODE_ARTICLE = 'article';
    const DISPLAY_MODE_ISSUE = 'issue';
    
    const DISPLAY_LOC_TOP = 'top';
    const DISPLAY_LOC_LEFT = 'left';
    const DISPLAY_LOC_RIGHT = 'right';

    protected $display = 'article';

    private $articlesTop = [];
    private $articlesLeft = [];
    private $articlesRight = [];

    private $sorted = false;

    /* Iterator */
    public function rewind()  {
        $this->articlesPosition = 0;
    }
    public function current() {
        $this->sortArticles();
        
        return $this->articles[array_merge($this->articlesTop, $this->articlesLeft, $this->articlesRight)[$this->articlesPosition]];
    }
    public function key() {
        return $this->articlesPosition;
    }
    public function next() {
        ++$this->articlesPosition;
    }
    public function valid() {
        $this->sortArticles();
        
        return isset(array_merge($this->articlesTop, $this->articlesLeft, $this->articlesRight)[$this->articlesPosition]);
    }


    /* */
    
    public function getSortedArticle($articleNum) {
        $this->sortArticles();
        

    }

    public function getDisplay() {
        return $this->display;
    }

    public function getParam($param) {
        if (!isset($this->params[$param])) {
            if ($param === 'issueID' && isset($this->params['articleID'])) { // This is a convenience feature, but it comes with the caveat that the returned value behaves almost identically to what the issueID actually would, the valid() and toString() methods will be different.
                return $this->params['articleID'];
            }
            else {
                throw new Exception($param . ' not in ' . implode(',', array_keys($this->params)));
            }
        }
        else {
            return $this->params[$param];
        }
    }

    public function getFirstArticle() {
        $this->sortArticles();

        if (count($this->articlesTop) > 0)
            return $this->articles[$this->articlesTop[0]];
        else if (count($this->articlesLeft) > 0)
            return $this->articles[$this->articlesLeft[0]];
        else if (count($this->articlesRight) > 0)
            return $this->articles[$this->articlesRight[0]];
        else
            throw new Exception('No articles.');
    }

    public function getArticleWithID($articleID) {
        foreach ($this->articles AS $article) {
            if ($article->getArticleString() === $articleID->getArticleString) return $article;
        }

        return false;
    }

    public function replaceWithArticleWithID($articleID) {
        $this->articles = [$this->getArticleWithID($articleID)];
        $this->sorted = false;
    }

    public function display() {
        if (isset($_GET['tabular'])) {
            echo '<table><tr><th>Article Title</th><th>Article Authors</th><th>Display Mode</th><th>Delete</th></tr>';
            foreach ($this->articles AS $article) {
                $this->displayArticle($article);
            }
            echo '</table>';
        }

        else {
            $this->sortArticles();

            echo '<div id="articlesGroup">';

                echo '<div class="topBlock">';
                foreach ($this->articlesTop AS $articleID)
                    $this->displayArticle($this->articles[$articleID], $this::DISPLAY_LOC_TOP);
                echo '</div>';

                echo '<div class="bottomBlock">';
                    echo '<div class="leftBlock">';
                    foreach ($this->articlesLeft AS $articleID)
                        $this->displayArticle($this->articles[$articleID], $this::DISPLAY_LOC_LEFT);
                    echo '</div>';

                    echo '<div class="rightBlock">';
                    foreach ($this->articlesRight AS $articleID)
                        $this->displayArticle($this->articles[$articleID], $this::DISPLAY_LOC_RIGHT);
                    echo '</div>';
                echo '</div>';

            echo '</div>';
        }
    }

    protected function parseParams($params) {
        if (isset($params['authorID']) && $params['authorID']->isValid())
            $this->display = $this::DISPLAY_MODE_AUTHOR;
        elseif (isset($params['issueID']) && $params['issueID']->isValid()) {
            $this->display = $this::DISPLAY_MODE_ISSUE;
        }
        elseif (isset($params['articleID']) && $params['articleID']->isValid()) {
            $this->display = $this::DISPLAY_MODE_ARTICLE;
        }
        else
            throw new Exception('Invalid $params array.');

        $this->params = $params;
    }

    private function sortArticles() {
        if ($this->sorted === true) return;
        else {
            $articlesCount = count($this->articles);

            // In C or C++, these would be pointer arrays. Instead, for PHP, we fill them with index keys that correspond with values in $this->articles
            $articlesTop = [];
            $articlesLeft = [];
            $articlesRight = [];
            $articlesLeft2 = [];
            $articlesRight2 = [];
            $articlesLeft3 = [];
            $articlesRight3 = [];
            $articlesFree = [];

            foreach ($this->articles AS $articleID => $article) {
                if ($this->display === $this::DISPLAY_MODE_AUTHOR
                    || $this->display === $this::DISPLAY_MODE_ARTICLE
                    || ($article->get('displayOptions') & DISPLAY_FORCE_LEFT
                        && $article->get('displayOptions') & DISPLAY_FORCE_RIGHT
                    )
                ) {
                    $articlesTop[] = $articleID;
                } elseif ($article->get('displayOptions') & DISPLAY_FORCE_LEFT) {
                    if ($article->get('displayOptions') & DISPLAY_PREFER_TOP)
                        $articlesLeft[] = $articleID;
                    else
                        $articlesLeft3[] = $articleID;
                } elseif ($article->get('displayOptions') & DISPLAY_FORCE_RIGHT) {
                    if ($article->get('displayOptions') & DISPLAY_PREFER_TOP)
                        $articlesRight[] = $articleID;
                    else
                        $articlesRight3[] = $articleID;
                } else {
                    $articlesFree[] = $articleID;
                }
            }

            $articlesCount -= count($articlesTop); // This should only be the total number of articles *NOT* at the top.

            foreach ($articlesFree AS $articleID) {
                if ((count($articlesTop)) < 1) {
                    $articlesTop[] = $articleID;
                    $articlesCount--; // This should only be the total number of articles *NOT* at the top.
                } elseif ((1 + count($articlesLeft) + count($articlesLeft2) + count($articlesLeft3)) < $articlesCount * .62)
                    $articlesLeft2[] = $articleID;
                else
                    $articlesRight2[] = $articleID;
            }

            $this->articlesTop = $articlesTop;
            $this->articlesLeft = array_merge($articlesLeft, $articlesLeft2, $articlesLeft3);
            $this->articlesRight = array_merge($articlesRight, $articlesRight2, $articlesRight3);

            // Mark completion
            $this->sorted = true;
        }
    }

    public function excerptArticleContent($articleContent, $articleLink, $threshold = EXCERPT_THRESHOLD) {
        /* Dom Traversal */
        $dom = new DOMDocument('1.0', 'UTF-8');
        $dom->loadHTML('<?xml encoding="utf-8" ?>' . $articleContent, LIBXML_HTML_NOIMPLIED | LIBXML_HTML_NODEFDTD | LIBXML_NOENT | LIBXML_NOXMLDECL);

        $domElemsToRemove = array();
        $count = 0;
        $removeEverythingElse = false;
        $keepNext = false;
        foreach ($dom->childNodes AS $p) {
            // Once removeEverythingElse has been toggled, we remove all remaining elements we encounter
            if ($removeEverythingElse) {
                $domElemsToRemove[] = $p;
            }

            // Only display the first styleBlock we encounter
            elseif (
                isset($p->tagName)
                && ($p->tagName === 'div')
                && ($p->getAttribute('class') === 'styleBlock')
            ) {
                $removeEverythingElse = true;
            }

            // Process all elements that have a tagName (p, div, etc.)
            elseif (
                isset($p->tagName)
            ) {
                if (($p->tagName === 'div' && $p->getAttribute('class') === 'slider')) { // sliders have a fixed increase
                    $increase = EXCERPT_BONUS_PER_SLIDER;
                }
                else { // calculate the increase for non-sliders
                    $increase = mb_strlen($p->textContent) + EXCERPT_BONUS_PER_ITEM;
                }

                // If the next conditions match, we will mark this loop's element for removal, unless keepNext was set true, in which case we toggle it off
                if (
                    $count > $threshold // We first check if count was already exceeding the threshold prior to this iteration of the loop.
                    || ( // If it wasn't, we:
                        ($count += $increase) > $threshold // Increase the count, and check again if it exceeds the threshold
                        && ($count > $threshold + EXCERPT_FLEX) // But make sure that, if the count now exceeds the threshold, the difference doesn't exceed the allowed FLEX (if it does, we keep the element)
                    )
                ) {
                    if (!$keepNext)
                        $domElemsToRemove[] = $p;
                    else
                        $keepNext = false;
                }
            }

            // Make sure we don't leave a hanging header
            elseif (
                isset($p->tagName)
                && ($p->tagName === 'h2' || $p->tagName === 'h3')
            ) {
                $keepNext = true;
            }
        }

        if (!empty($domElemsToRemove)) {
            foreach ($domElemsToRemove as $domElement) {
                $domElement->parentNode->removeChild($domElement);
            }

            $readArticleLink = $dom->createElement('a', 'Read The Full Article');
            $readArticleLink->setAttribute('href', $articleLink);
            $readArticleLink->setAttribute('class', 'readFullArticle');
            $dom->appendChild($readArticleLink);
        }

        return html_entity_decode($dom->saveHTML(), ENT_NOQUOTES);
    }
    
    
    private function filter($content, $displayLocation) {        
        return $content;
    }


    private function displayArticle(Article $article, $displayLocation) {
        if (isset($_GET['tabular'])) {
            echo '<tr><td><a href="' . $article->get('absoluteLink') . '">' . $article->get('title') . '</a></td><td>' . $article->getAuthorStringAnchored() . '</td><td>' . $article->get('displayOptions') . ':
                        <select name="value" onchange="this.form.submit()">
                            <option value=""></option>
                            <option value="none">Automatic</option>
                            <option value="left">Left</option>
                            <option value="lefttop">Left Top</option>
                            <option value="right">Right</option>
                            <option value="righttop">Right Top</option>
                            <option value="top">Top</option>
                        </select></td><td><form method="post" action="' . $article->getArticleURL() . '/delete" onsubmit="return confirm(\'This will permanently delete the current article. Are you sure you wish to continue?\');" class="centred"><input type="submit" name="deleteArticle" value="Delete This Article" /></form></td></tr>';
        }
        else {
            echo '
            <div class="article">
                <article' . ((($this->display === $this::DISPLAY_MODE_ISSUE || $this->display === $this::DISPLAY_MODE_AUTHOR) && (strlen($article->get('content')) > EXCERPT_THRESHOLD)) ? ' class="excerpted"' : '') . '>
                    <header>
                        <h2><a href="' . $article->get('absoluteLink') . '">' . $article->get('title') . '</a></h2>
                        <span class="author">by ' . $article->getAuthorStringAnchored() . '</span>';

            if ($this->display === $this::DISPLAY_MODE_AUTHOR)
                echo ', <span class="date"><a href="' . $article->getIssueURL() . '">' . $article->getIssueName() . '</a></span>';

            if (isAdmin() && get_class($this) === 'ArticleBuilderDatabase') {
                switch ($this->display) {
                    case $this::DISPLAY_MODE_ISSUE:
                        echo '<form method="post" action="' . $article->getArticleURL() . '/delete" onsubmit="return confirm(\'This will permanently delete the current article. Are you sure you wish to continue?\');" class="centred"><input type="submit" name="deleteArticle" value="Delete This Article" /></form><form method="post" action="/admin.php?action=updatePosition&year=' . $article->getYear() . '&month=' . $article->getMonth() . '&item=' . $article->getArticleNumString() . '" class="centred">
                        Update Display (' . $article->get('displayOptions') . '):
                        <select name="value" onchange="this.form.submit()">
                            <option value=""></option>
                            <option value="none">Automatic</option>
                            <option value="left">Left</option>
                            <option value="lefttop">Left Top</option>
                            <option value="right">Right</option>
                            <option value="righttop">Right Top</option>
                            <option value="top">Top</option>
                        </select>
                     </form>';
                        break;

                    case $this::DISPLAY_MODE_ARTICLE:
                        break;
                }
            }

            echo '
                    </header>

                    <div class="mainContent">';

            if ($this->display === $this::DISPLAY_MODE_ARTICLE || ($this->display === $this::DISPLAY_MODE_AUTHOR && isset($_GET['full'])))
                echo $this->filter($article->get('content'), $displayLocation);
            else
                echo $article->get('excerpt');

            echo '
                    </div>
                </article>
            </div>';
        }
    }
}

class ArticleBuilderDatabase extends ArticleBuilder {
    public function __construct(database $database, $params) {
        /* Parse $params */
        $this->parseParams($params);


        /* Fetch Articles */
        // Where Conditions
        $articlesWhere = [
            'aauthorID' => $database->col('authorID'),
            'aissue' => $database->col('issue'),
            'aarticleID' => $database->col('articleID')
        ];

        switch ($this->display) {
            case $this::DISPLAY_MODE_AUTHOR:
                $articlesWhere['authorID'] = $params['authorID']->id();
                break;
            case $this::DISPLAY_MODE_ARTICLE:
                $articlesWhere['issue'] = $params['articleID']->getIssueString();
                $articlesWhere['articleID'] = $params['articleID']->articleNum;
                break;
            case $this::DISPLAY_MODE_ISSUE:
                $articlesWhere['issue'] = $params['issueID']->getIssueString();
                break;
            default:
                throw new Exception('Bad data.');
        }

        // Sort
        $articlesSort = [];
        if ($this->display === 'author')
            $articlesSort['issue'] = 'desc';
        else
            $articleSort['articleID'] = 'asc';

        // Fetch
        $articlesByIssue = $database->select([
            'new_authors' => 'authorID, authorName, facebook authorFacebook',
            'new_articles' => 'issue, articleID, title, content, excerpt, displayOptions', // TODO: facebook
            'new_articleAuthors' => 'issue aissue, articleID aarticleID, authorID aauthorID',
        ], $articlesWhere, $articlesSort)->getAsArray(['issue', 'articleID', 'authorID'], false);

        if (empty($articlesByIssue))
             throw new Exception('No articles found.');

        // Build Articles from Fetch
        foreach ($articlesByIssue AS $issue => $articlesByID) { // drill into issue
            foreach ($articlesByID AS $articleID => $articlesByAuthor) { // drill into articleID
                $articleAuthors = [];
                foreach ($articlesByAuthor AS $authorID => $article) { // drill into authorID
                    $articleAuthors[] = new Author($database, $article['authorID'], $article['authorName']);
                }

                $article = reset($articlesByAuthor);
                $article['authors'] = $articleAuthors;
                $this->articles[] = new Article(IssueID::YearFromIssueString($article['issue']), IssueID::MonthFromIssueString($article['issue']), $article['articleID'], $article);
            }
        }
    }
}

class ArticleBuilderHTML extends ArticleBuilder {
    public function __construct(phpTypography $typo, $params) {
        $this->parseParams($params);

        if ($this->display === self::DISPLAY_MODE_AUTHOR)
            throw new Exception('Author pages are not supported by the HTML fetcher.');
        else {
            if ($this->display === self::DISPLAY_MODE_ISSUE) {// Get all files in the issue if we haven't selected an individual item, or if buildTree is true (meaning we need to rebuild the title cache.)
                if (is_dir($params['issueID']->getIssuePath()))
                    $files = scandir($params['issueID']->getIssuePath());
                else
                    throw new Exception('Invalid issue.');
            }
            else if ($this->display === self::DISPLAY_MODE_ARTICLE) { // Otherwise, just get the one file.
                if (is_file($params['articleID']->getArticlePath()))
                    $files = array(basename($params['articleID']->getArticlePath()));
                else
                    throw new Exception('Invalid article.');
            }
            else {
                throw new Exception('bad data.');
            }
        }


        if (empty($files))
            throw new Exception('No articles found.');


        // Build the $articles array from HTML files
        foreach ($files AS $file) {
            if ($file === '.' || $file === '..') continue;
            if ($file === '__titleCache.html') continue; // This is an HTML cache file
            if (substr($file, -5) !== '.html') continue; // Allows images, etc. in the issue folder

            if (preg_match('/^\d{4}-\d{2}-01-\d{3}\.html$/', $file) === 0) continue; //die("Improper filename $file found in data scraping. Aborting."); // Sanity checking.
            $articleID = new ArticleID(ArticleID::yearFromArticleString($file), ArticleID::monthFromArticleString($file), ArticleID::articleNumFromArticleString($file));

            $contents = file_get_contents($this->getParam('issueID')->getIssuePath() . $file) or die('Failed opening file for reading: ' . $file);

            // Article Title
            if (preg_match('/\<h1 class="articletitle"\>(.*?)\<\/h1\>/s', $contents, $title) === 1)
                $title = $title[1];
            else
                die("Title not found when analysing $file. Aborting.");

            // Column Title
            if (preg_match('/\<span class="columnname"\>(.*?)\<\/span\>/s', $contents, $columnTitle) === 1)
                $columnTitle = $columnTitle[1];

            // Authors
            $authorMatches = [];
            preg_match_all('/<span class="authorname"\>(.*?)\<\/span\>/s', $contents, $authorMatches, PREG_SET_ORDER);

            $authors = [];
            foreach ($authorMatches AS $authorMatch) {
                $authors[] = new Author(null, 0, $authorMatch[1]);
            }

            if (count($authors) === 0) die("Authors not found when analysing $file. Aborting.");

            // Issue Date
            if (preg_match('/\<span class="issuedate"\>(.*?)\<\/span\>/s', $contents, $issuedate) === 1)
                $issuedate = $issuedate[1];
            else
                die("Date not found when analysing $file. Though this can be inferred from the directory path, we enforce its inclusion as a sanity cehcking. Aborting.");

            $issuemonth = date("m", strtotime(preg_replace('/(\w*) (\d{4})/', '$1', $issuedate)));
            $issueyear = preg_replace('/(\w*) (\d{4})/', '$2', $issuedate);

            if ($issuemonth != $articleID->month)
                die("Issue date mismatch: expected month {$articleID->month}, found $issuemonth (file=$file)");
            if ($issueyear != $articleID->year)
                die("Issue date mismatch: expected year {$articleID->year}, found $issueyear (file=$file)");

            // Link
            $link = $articleID->getArticleURL();

            // Core Content
            $content = trim(preg_replace('/.*\<article\>(.*?)\<\/article\>.*/s', '$1', $contents));
            $content = preg_replace('/\<header\>(.*?)\<\/header\>/s', '', $content); // Remove header from core content
            $content = str_replace(['<h4', '<h3', '<h2'], ['<h5', '<h4', '<h3'], $content); // <h1> is reserved for page titles, and <h2> for article titles, while the article format assumes that <h1> would be reserved for article titles, and <h2> would be unreserved.
            $content = str_replace('<a ', '<a target="_blank" ', $content); // The articles should not specify this attribute, but we want it all the same.
            $content = str_replace('"images/', '"/issue/' . $articleID->year . '/' . sprintf('%02d', $articleID->month) . '/images/', $content);
            $content = str_replace('id="slider"', 'class="slider"', $content); // Temporary, until I manage to get the articles REGEXable. (TODO)
            $content = @$typo->process($content);
            $excerpt = $this->excerptArticleContent($content, $link);

            $this->articles[] = new Article($articleID->year, $articleID->month, $articleID->articleNum, array(
                'content' => $content,
                'excerpt' => $excerpt,
                'columnTitle' => $columnTitle,
                'title' => $title,
                'authors' => $authors,
                'month' => (int)$issuemonth,
                'year' => (int)$issueyear,
                'issue' => $articleID->getIssueName(),
                'absoluteLink' => $link
            ));
        }

    }
}
?>
