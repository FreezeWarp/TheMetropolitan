<?php
/* TODO: HTML fetching. */
$htmlPageData = ['title' => 'Select an Issue'];
require('global.php'); // global
require('templateHead.html'); // HTML

if (isAdmin()) {
    echo '<br />
<form method="post" action="./add" class="centred">
    Add New Issue:
    <select name="year">
        <option value="1999">1999</option>
        <option value="2000">2000</option>
        <option value="2001">2001</option>
        <option value="2002">2002</option>
        <option value="2003">2003</option>
        <option value="2004">2004</option>
        <option value="2005">2005</option>
        <option value="2006">2006</option>
        <option value="2007">2007</option>
        <option value="2008">2008</option>
        <option value="2009">2009</option>
        <option value="2010">2010</option>
        <option value="2011">2011</option>
        <option value="2012">2012</option>
        <option value="2013">2013</option>
        <option value="2014">2014</option>
        <option value="2015">2015</option>
        <option value="2016">2016</option>
    </select>
    <select name="month">
        <option value="1">January</option>
        <option value="2">February</option>
        <option value="3">March</option>
        <option value="4">April</option>
        <option value="5">May</option>
        <option value="6">June</option>
        <option value="7">July</option>
        <option value="8">August</option>
        <option value="9">September</option>
        <option value="10">October</option>
        <option value="11">November</option>
        <option value="12">December</option>
    </select>
    <input type="submit" value="Go" />
</form>';
}

$issues = $database->select([
    'new_issues' => 'issue date'
])->getAsArray(true);

echo '<div class="letter"><h1>Issues: </h1><ul>';
foreach ($issues AS $issue) {
    $issueID = new IssueID(IssueID::yearFromIssueString($issue['date']), IssueID::monthFromIssueString($issue['date']));

    echo '<li><a href="' . $issueID->getIssueURL() .  '">' . $issueID->getIssueName() . '</a> (Also available in <a href="' . $issueID->getIssueURL() .  'issue.pdf">PDF Format</a>)';
}
echo '</ul></div>';

require('templateFoot.html'); // HTML
?>