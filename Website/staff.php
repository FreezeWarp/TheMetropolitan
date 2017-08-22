<?php

$htmlPageData = ['title' => 'Staff'];
require('global.php'); // global
require('templateHead.html'); // HTML

?>

<div class="letter">
    <div>
        <h1 class="extendedTitle"><a href="/author/1006/">Kathryn Ganfield</a><br /><small class="subtitle">Editor-in-chief</small></h1>
        Kathryn Ganfield is a Creative Writing major and Research &amp; Information Studies minor. In addition to journalism, she focuses on memoir, nature and outdoor writing. A personal essay is forthcoming in Haute Dish. She recently completed her training as a Minnesota Master Naturalist volunteer. She lives in St. Paul with her family. 
    </div>

    <div>
        <h1 class="extendedTitle"><a href="/author/1001/">Scott Lindell</a><br /><small class="subtitle">Associate editor</small></h1>
        Scott Lindell is a Professional Writing and Technical Communications major. Since July 2016, he has written articles and served as Layout Editor for The Metropolitan. He enjoys writing about student clubs and community organizations. He is a skilled copywriter of press releases, ads, brochures, newsletters, memos, instructions and websites. With over 18 years of retail management experience, he currently works as an assistant manager for Famous Footwear.
    </div>

    <div>
        <h1 class="extendedTitle"><a href="/author/1028/">C.T. Corum</a><br /><small class="subtitle">Copy editor</small></h1>
        C.T. Corum is a Professional Writing and Technical Communications major and Creative Writing minor. He has written for The Metropolitan since November 2016. In addition to writing stories and pursuing a college degree, C.T. Corum also works as a manager at Culver’s. 
    </div>

    <div>
        <h1 class="extendedTitle"><a href="/author/1028/">Dominique Hlaavc</a><br /><small class="subtitle">Layout editor</small></h1>
    </div>

    <div>
        <img src="/headshots/1029-small.jpg" class="headshot" style="float: left; width: 30px; padding: 2px;">
        <h1 class="extendedTitle"><a href="/author/1029/">Joseph Parsons</a><br /><small class="subtitle">Online administrator</small></h1>
        The current online administrator, Joseph is a "mediocre writer, pretty okay programmer" (his words) who has been tasked with the continued development of The Metropolitan Online. He designed the "issue" layout of multiple excerpted articles on one page, the author pages, the AJAX mechanism that causes pages to load seemlessly into eachother, and the Facebook friendly markup that makes it easy to post our individual articles to social media.
    </div>

    <h1>Additional Website Credits</h1>
    <p>Our website, The Metropolitan Online, was originally developed by <strong><a href="/author/1005/">Levi King</a></strong>, who thought that our previous approach -- posting chunky PDFs on our hard-to-access OrgSync page -- was limiting online readership. As part of his capstone project, he designed and built The Metropolitan Online around a simple database structure still in use today.</p>
    <p>Helping him were <strong>Ben Michaels and Seth Moudry</strong>, who together designed a set of Java tools to easily convert Doc and DocX files into an HTML archive format that could be easily imported into The Metropolitan's database. Thanks to this, several years worth of articles (over a thousand) are now being slowly added to The Metropolitan Online, many of which weren't previously available online.</p>
</div>

<?php

require('templateFoot.html'); // HTML
?>
