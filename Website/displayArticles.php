<?php
/* Display Selection */
require('functions.php');

if (isset($_GET['author'])) {
    $display = 'author';
    $params = [
        'authorID' => new Author(null, (int) $_GET['author'], false),
    ];
}
else if (isset($_GET['year'], $_GET['month'])) {
    if (isset($_GET['item'])) {
        $display = 'single';
        $params = [
            'articleID' => new ArticleID((int) $_GET['year'], (int) $_GET['month'], (int) $_GET['item']),
        ];

    }
    else {
        $display = 'issue';
        $params = [
            'issueID' => new IssueID((int) $_GET['year'], (int) $_GET['month']),
        ];
    }
}
else {
    $params = [
        'issueID' => new IssueID(2016, 6),
    ];
    $display = 'issue';
}


/* Do any of these take a lot of time? No. But they do make a handful of DB queries, and they use a fair bit of memory to sort through articles. Thus, we do save a lot of effort during peak load by using simple caching. (Naturally, a memory cache would be preferred and could likely be implemented with < 100MB, but without APC I don't really have that luxury.) */
switch ($display) {
    case 'issue':
        $cachePath = 'cache/' . $params['issueID']->getIssuePath() . 'index.html';
        break;

    case 'author':
        $cachePath = 'cache/' . $params['authorID']->getAuthorPath() . 'index' . (isset($_GET['full']) ? '.full' : '') . '.html';
        break;

    case 'single':
        $cachePath = 'cache/' . $params['articleID']->getArticlePath();
        break;
}

if (!isAdmin() && !isset($_GET['nocache']) && file_exists($cachePath)) { // If we're an admin, we will be outputting administrative controls; if nocache is set, we want to refresh (TODO: do this through admin.php or similar)
    $handle = fopen($cachePath, "r");
    while (!feof($handle))
        echo fread($handle, 8192); // Note that we use fread instead of file_get_contents to benefit from its reduced memory usage.
    fclose($handle);
}
else {
    if (!isAdmin()) // Only cache if not admin (which will have additional administrative controls in the output)
        ob_start(function($output) {
            echo 'Caching...';
            global $cachePath;

            if (!is_dir(dirname($cachePath)))
                mkdir(dirname($cachePath), 0755, true);

            file_put_contents($cachePath, $output);

            return $output;
        });
    else
        ob_start();

    require('global.php'); // global stuff

    /* The data is stored in two places here: the database, and in the server's HTML store.
     * We could query from either, but using the database is easier and faster. However, the HTML store will work if we don't somehow have database, but it is much slower, and not implemented as effeciently as the DB code. */
    /* HTML Article Builder */
    if ($mode === 'html' || $mode === 'import') {
        include('libs/php-typography/php-typography.php');

        if (!file_exists(current($params)->getIssuePath() . '__titleCache.html') || $mode === 'import')
            $buildTree = true;
        else
            $buildTree = false;

        if (isset($params['articleID']) && $buildTree) {
            $oldArticleID = $params['articleID'];
            $params = ['issueID' => $oldArticleID->getIssueIDCopy()];
        }

        $articles = new ArticleBuilderHTML(new phpTypography(), $params);
        if ($buildTree) {
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

            // Cache the title tree, but only if we are in HTML (not import) mode.
            if ($mode === 'html')
                file_put_contents($articles->getParam('issueID')->getIssuePath() . '__titleCache.html', $header);

            // If we are in single mode, but we retrieved all articles in order to build the title cache, then update $articles with only the desired entry.
            if ($display === 'single')
                $articles = $articles->replaceWithArticleWithID($oldArticleID);
        }
        else {
            $header  = file_get_contents($articles->getParam('issueID')->getIssuePath() . '__titleCache.html');
        }


        /* Database Update (for import) */
        if ($mode === 'import') {
            // Issue -- will create if it doesn't exist, will update titleCache if it does.
            $database->upsert('new_issues', [
                'issue' => $articles->getParam('issueID')->getIssueString(),
            ], [
                'titleCache' => $header,
            ]);


            // Articles and Authors
            foreach ($articles AS $article) {
                // If article data (identified by unique issue + article ID) already exists, update it. Otherwise, insert it.
                $database->upsert('new_articles', [
                    'issue' => $article->getIssueString(),
                    'articleID' => (int) $article->articleNum,
                ], [
                    'title' => $article->title,
//                    'columnTitle' => $article->columnTitle,
                    'content' => $article->content,
                    'excerpt' => $article->excerpt
                ]);


                // Delete, Insert Article Authors
                $database->delete('new_articleAuthors', [
                    'issue' => $article->getIssueString(),
                    'articleID' => $article->articleNum,
                ]);
                foreach ($article->authors AS $author) {
                    if (!$authorID = $database->select(['new_authors' => 'authorID, authorName'], [ // Fetch author data
                        'authorName' => $author->getAuthorName()
                    ])->getColumnValue('authorID')) {
                        if ($database->insert('new_authors', [ // If none exists, create new author data
                            'authorName' => $author->getAuthorName()
                        ])) {
                            $authorID = $database->insertId;
                        } else {
                            die('Author insert failed.');
                        }
                    }

                    $database->insert('new_articleAuthors', [
                        'issue' => $article->getIssueString(),
                        'articleID' => $article->articleNum,
                        'authorID' => $authorID,
                    ]);
                }
            }
        }
    }

    elseif ($mode === 'database') {
        $articles = new ArticleBuilderDatabase($database, $params);

        switch ($articles->getDisplay()) {
            case $articles::DISPLAY_MODE_AUTHOR:
                $author = new Author($database, $articles->getParam('authorID')->id());
                $header = $author->getAuthorPageHeader();
                break;
            case $articles::DISPLAY_MODE_ISSUE:
            case $articles::DISPLAY_MODE_ARTICLE:
                $header = $articles->getParam('issueID')->fetchTitleCache($database);
                break;
            default:
                throw new Exception('Bad data.');
        }
    }


    /* HTML Template Variables */
    switch ($display) {
        case 'single':
            // Find the first image tag, extract its URL into {$imageMatch} (for OG image)
            if (preg_match('/\<img[^\>]+src="([^"]+?)"[^\>]+(\/|)\>/is', $articles->getFirstArticle()->content, $imageMatch) === 0)
                $imageMatch = false;
            else
                $imageMatch = 'http://themetropolitan.metrostate.edu' . $imageMatch[1] . '.width=600';

            // Find the first p tag, extract its contents in {$pMatch} (for OG description)
            if (preg_match('/\<p\>(.+?)\<\/p\>/is',  $articles->getFirstArticle()->excerpt, $pMatch) === 0)
                $pMatch = '';
            else
                $pMatch = $pMatch[1];

            // Build $htmlPageData array from article contents and two extractions above
            $htmlPageData = [
                'title' =>  $articles->getFirstArticle()->title,
                'description' => strip_tags($pMatch), // strip_tags removes extraneous formatting (spans, ems, etc.)
                'image' => buildImageDataArray($imageMatch),
                'author' => ['name' => $articles->getFirstArticle()->authors[0]->getAuthorName() , 'facebook' => $articles->getFirstArticle()->authors[0]->facebook]
            ];

            if (isAdmin()) {
                $header .= '<form method="post" action="./edit" class="centred"><input type="button" name="editArticle" value="Edit This Article" /></form><form method="post" action="./delete" onsubmit="return confirm(\'This will permanently delete the current article. Are you sure you wish to continue?\');" class="centred"><input type="submit" name="deleteIssue" value="Delete This Article" /></form>';
            }
            break;

        case 'issue':
            $titlesArray = array();
            preg_match_all('/\<span class="title"\>(.+?)\<\/span\>/', $header, $titlesArray);
            array_walk($titlesArray[1], 'trim');

            // Build $htmlPageData array from issue data
            $htmlPageData = [
                'title' => $articles->getParam('issueID')->getIssueName(),
                'image' => buildImageDataArray(false),
                'description' => implode(' ⸱ ', $titlesArray[1])
            ];

            if (isAdmin()) {
                $header .= '<form method="post" action="./add" class="centred"><input type="button" name="addArticle" value="Add New Article to This Issue" /></form><form method="post" action="./delete" onsubmit="return confirm(\'This will permanently delete the current issue and all articles attached to it. This action will be logged, but article restoration will only be possible manually. Are you sure you wish to continue?\');" class="centred"><input type="submit" name="deleteIssue" value="Delete This Issue" /></form>';
            }
            break;

        case 'author':
            // Build $htmlPageData array from author data
            $htmlPageData = [
                'title' => $author->getAuthorName(),
                'description' => '',
                'image' => buildImageDataArray('http://themetropolitan.metrostate.edu/headshots/' . $author->id() . '.jpg'),
                'author' => ['name' => $author->getAuthorName(), 'facebook' => $author->facebook]
            ];

            if (isAdmin()) {
                $header .= '<form method="post" action="./edit" class="centred"><input type="button" name="editAuthor" value="Edit This Author" /></form>';
            }
            break;
    }


    /* Output */
    require('templateHead.html'); // HTML template
    echo $header;
    $articles->display();
    require('templateFoot.html'); // HTML
    ob_end_flush();
}
?>
