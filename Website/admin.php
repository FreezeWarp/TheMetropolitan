<?php
require('global.php');

if (!isAdmin()) {
    die('No.');
}
else {
    switch ($_GET['action']) {
        case 'getAuthors':
            die(json_encode($database->where(['authorName' => $database->search($_REQUEST['authorName'])])->select(['new_authors' => 'authorName'])->getColumnValues('authorName')));
            break;


        case 'addIssue':
            $issue = new IssueID($_POST['year'], $_POST['month']);

            $database->insert('new_issues', [
                'issue' => $issue->getIssueString(),
            ]);

            $database->insert('new_articles', [
                'issue' => $issue->getIssueString(),
                'articleID' => 0,
                'title' => 'Article #1',
                'content' => 'This is the first article of the issue. You should edit or delete it.',
                'excerpt' => 'This is the first article of the issue. You should edit or delete it.',
            ]);

            $database->insert('new_articleAuthors', [
                'issue' => $issue->getIssueString(),
                'articleID' => 0,
                'authorID' => 1,
            ]);

            header('Location: ' . $issue->getIssueURL());
            break;


        case 'deleteIssue':
            $issue = new IssueID($_GET['year'], $_GET['month']);
            $database->startTransaction();

            $database->delete('new_articleAuthors', [
                'issue' => $issue->getIssueString(),
            ]);

            $database->delete('new_articles', [
                'issue' => $issue->getIssueString(),
            ]);

            $database->delete('new_issues', [
                'issue' => $issue->getIssueString(),
            ]);

            $database->endTransaction();
            header('Location: /');
            break;

        case 'addArticle':
            $issue = new IssueID($_GET['year'], $_GET['month']);
            $database->startTransaction();

            $articleID = $database->where(['issue' => $issue->getIssueString()])->select(['new_articles' => ['issue', 'articleID', 'articleIDMax' => $database->type('equation', 'MAX($articleID)')]])->getColumnValue('MAX(articleID)') + 1; // A somewhat temporary solution.

            $database->insert('new_articles', [
                'issue' => $issue->getIssueString(),
                'articleID' => (int) $articleID,
                'title' => $_POST['title'],
                'content' => $_POST['content'],
                'excerpt' => $_POST['content'],
            ]);

            foreach ($_POST['author'] AS $authorName) {
                if (!$authorID = $database->select(['new_authors' => 'authorID, authorName'], [ // Fetch author data
                    'authorName' => $authorName
                ])->getColumnValue('authorID')) {
                    if ($database->insert('new_authors', [ // If none exists, create new author data
                        'authorName' => $authorName
                    ])) {
                        $authorID = $database->insertId;
                    } else {
                        //$database->rollbackTransaction();
                        die('Author insert failed.');
                    }
                }

                $database->insert('new_articleAuthors', [
                    'issue' => $issue->getIssueString(),
                    'articleID' => (int) $articleID,
                    'authorID' => $authorID,
                ]);
            }

            $issue->refreshTitleCache($database);
            $database->endTransaction();
            header('Location: ' . $_SERVER['HTTP_REFERER'] . '/' . $articleID);
            break;

        case 'deleteArticle':
            $issue = new IssueID($_GET['year'], $_GET['month']);

            $database->startTransaction();

            $database->delete('new_articleAuthors', [
                'issue' => $issue->getIssueString(),
                'articleID' => $_GET['item']
            ]);

            $database->delete('new_articles', [
                'issue' => $issue->getIssueString(),
                'articleID' => $_GET['item']
            ]);

            $issue->refreshTitleCache($database);
            $database->endTransaction();

            header('Location: '. $issue->getIssueURL());
            break;

        case 'updateArticle':
            $issue = new IssueID($_GET['year'], $_GET['month']);
            $database->startTransaction();

            $database->update('new_articles', [
                'title' => $_POST['title'],
                'content' => $_POST['content'],
            ], [
                'issue' => $issue->getIssueString(),
                'articleID' => (int) $_GET['item']
            ]);

            $database->delete('new_articleAuthors', [
                'issue' => $issue->getIssueString(),
                'articleID' => $_GET['item'],
            ]);

            foreach ($_POST['author'] AS $authorName) {
                if (!$authorID = $database->select(['new_authors' => 'authorID, authorName'], [ // Fetch author data
                    'authorName' => $authorName
                ])->getColumnValue('authorID')) {
                    if ($database->insert('new_authors', [ // If none exists, create new author data
                        'authorName' => $authorName
                    ])) {
                        $authorID = $database->insertId;
                    } else {
                        $database->rollbackTransaction();
                        die('Author insert failed.');
                    }
                }

                $database->insert('new_articleAuthors', [
                    'issue' => $issue->getIssueString(),
                    'articleID' => (int) $_GET['item'],
                    'authorID' => $authorID,
                ]);
            }

            $issue->refreshTitleCache($database);

            $database->endTransaction();

            header('Location: ' . $_SERVER['HTTP_REFERER']);
            break;

        case 'updateAuthor':
            if (!$database->update('new_authors', [ // If none exists, create new author data
                'twitter' => $_POST['twitter'],
                'facebook' => $_POST['facebook'],
                'linkedin' => $_POST['linkedin'],
                'email' => $_POST['email'],
                'website' => $_POST['website']
            ], [
                'authorID' => $_GET['author']
            ])) {
                die('Failed.');
            }

            header('Location: ' . $_SERVER['HTTP_REFERER']);
            break;

        case 'updatePosition':
            $issue = new IssueID($_GET['year'], $_GET['month']);
            
            switch($_POST['value']) {
                case 'none':
                    $value = 0;
                    break;
                case 'left':
                    $value = DISPLAY_FORCE_LEFT;
                    break;
                case 'lefttop':
                    $value = DISPLAY_FORCE_LEFT | DISPLAY_PREFER_TOP;
                    break;
                case 'right':
                    $value = DISPLAY_FORCE_RIGHT;
                    break;
                case 'righttop':
                    $value = DISPLAY_FORCE_RIGHT | DISPLAY_PREFER_TOP;
                    break;
                case 'top':
                    $value = DISPLAY_FORCE_TOP;
                    break;
                default:
                    die('Invalid value.');
                    break;
            }

            if (!$database->update('new_articles', [ // If none exists, create new author data
                'displayOptions' => $database->type('equation', '$displayOptions & ~' . (DISPLAY_FORCE_LEFT | DISPLAY_FORCE_RIGHT | DISPLAY_FORCE_TOP | DISPLAY_PREFER_TOP) . ' | ' . $value)
            ], [
                'articleID' => $database->int($_GET['item']),
                'issue' => $issue->getIssueString(),
            ])) {
                die('Failed.');
            }
            
            $issue->refreshTitleCache($database);

            header('Location: ' . $_SERVER['HTTP_REFERER']);
        break;

        default:
            die('No Action.');
            break;
    }
    exit;
}
