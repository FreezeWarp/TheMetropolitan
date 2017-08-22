<?php
/* This is a fairly simple sitemap that outputs every canonical URL we have */

header('content-type: text/plain');
require('global.php'); // global

echo "http://themetropolitan.metrostate.edu/\n";
echo "http://themetropolitan.metrostate.edu/issue/\n";

//TODO:add:
//echo "http://themetropolitan.metrostate.edu/author/\n";

foreach($database->select([
    'new_issues' => 'issue date'
])->getAsArray(true) AS $issue) {
    $year = substr($issue['date'], 0, 4);
    $month = substr($issue['date'], 5, 2);

    echo "http://themetropolitan.metrostate.edu/issue/$year/$month/\n";
}

foreach($database->select([
    'new_articles' => 'issue, articleID'
], null, ['issue' => 'asc', 'articleID' => 'asc'])->getAsArray(true) AS $article) {
    $year = substr($article['issue'], 0, 4);
    $month = substr($article['issue'], 5, 2);

    echo "http://themetropolitan.metrostate.edu/issue/$year/$month/" . sprintf("%03d", $article['articleID']) . "/\n";
}

foreach($database->select([
    'new_authors' => 'authorID'
])->getAsArray(true) AS $author) {
    echo "http://themetropolitan.metrostate.edu/author/{$author['authorID']}/\n";
}
?>