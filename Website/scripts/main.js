/* This super messy file should eventually be refactored. */


/* Global variables */
var cachedPages = {};
var a;
String.prototype.fromHTML = function(tag1, tag2) { return this.replace(a = new RegExp("([\\s\\S]*?)\\<" + tag1 + "\\>([\\s\\S]*)\\<\\/" + tag2 + "\\>([\\s\\S]*)"), "$2"); }

/* Page transition function */
/*function loadPage(pageUrl, history) {
    var history = typeof history === 'undefined' || history;

    content = false;

    xmlhttp = new XMLHttpRequest();
    xmlhttp.open("GET", pageUrl, true);
    xmlhttp.send();

    if (history &&
        pageUrl.match(/\/issue\/\d{4}\/\d{2}\//) &&
        window.location.href.match(/\/issue\/\d{4}\/\d{2}\//) && pageUrl.match(/\/issue\/\d{4}\/\d{2}\//)[0] === window.location.href.match(/\/issue\/\d{4}\/\d{2}\//)[0] &&
        document.getElementById("articlesGroup")) {
        var resizeTarget = document.getElementById("articlesGroup");
        var tag1 = 'div id="articlesGroup"';
        var tag2 = 'div';
    }
    else {
        var resizeTarget = document.getElementsByTagName("main")[0];
        var tag1 = '';
        var tag2 = '';
    }

    var resizeFactor = .96;
    resizeTarget.style.opacity = 1;

    var timer = setInterval(function() {
        resizeTarget.style.opacity = resizeFactor * resizeTarget.style.opacity;

        if (resizeTarget.style.opacity < .1) {
            clearInterval(timer);

            resizeTarget.innerHTML = "";

            function showNew(responseText, pageTitle) {
                if (history) {
                    window.history.pushState({
                        "html": responseText,
                        "pageTitle": pageTitle
                    }, pageTitle, pageUrl);
                }

                resizeTarget.innerHTML = responseText.fromHTML(tag1, tag2);
                $("img").load(function(event){
                    if ($(this).closest('.slider'))
                        $(this).closest('.slider').slick('setPosition');
                });

                document.title = pageTitle;

                parsePage();

                var timer2 = setInterval(function() {
                    resizeTarget.style.opacity = (2 - resizeFactor) * resizeTarget.style.opacity;

                    if (resizeTarget.style.opacity > .9999) {
                        clearInterval(timer2);

                        resizeTarget.style.opacity = 1;
                    }
                }, 10);



                if ((document.documentElement.scrollTop || document.body.scrollTop) > 350) {
                    $('html, body').animate({
                        scrollTop: $("#menu").offset().top
                    }, 500);
                }
            }

            function xmlReady() {
                if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                    content = xmlhttp.responseText.fromHTML('main', 'main');
                    pageTitle = xmlhttp.responseText.fromHTML('title', 'title');

                    cachedPages[pageUrl] = {
                        'content' : content,
                        'pageTitle' : pageTitle
                    }

                    showNew(content, pageTitle);

                    return true;
                }

                return false;
            }

            if (content) {
                showNew(content, pageTitle);
            }
            else if (!xmlReady()) {
                xmlhttp.onreadystatechange = xmlReady();
            }
        }
    }, 10);

    return false;
}*/


/* Use loadPage for history navigation. */
/*window.onpopstate = function(e) {
    loadPage(document.location.href, false);
};*/

/* Use loadPage when a link is clicked. */
/*document.addEventListener('click', function(e) {
    var target = e.target;
    window.y=e;

    while(target !== null && target.nodeName !== "A") {
        target = target.parentNode;
    }

    if (target === null)
        return;

    a = target;
    console.log(target);
    console.log(e);
    if (target.host === window.location.host && !target.getAttribute('href').match(/(\.pdf)$/) && !(e.ctrlKey || e.shiftKey || e.metaKey || e.which === 2)) {
        e.preventDefault();
        loadPage(target.getAttribute('href'))
    }
}, false); // Only works in newerish browsers. Old IE uses attachEvent instead.*/


/*
 * The goal here is to automatically remove paragraphs when either the left or right block is substantially larger than the other.
 * Note, of-course, that the excerpts themselves are all generated to be as roughly equal as possible.
 */
function resizePage() {
    clearTimeout(window.resizeTrigger);
    window.resizeTrigger = setTimeout(function() {
        $('.leftBlock').removeClass('shorten');
        $('.rightBlock').removeClass('shorten');
        $('.rightBlock').removeClass('shortenMore');
        
        var leftBlockHeight = $('.leftBlock').height(),
            rightBlockHeight = $('.rightBlock').height(),
            leftBlockSpacing = $('.leftBlock .article:eq(1)').offset().top - $('.leftBlock .article:eq(0)').offset().top - $('.leftBlock .article:eq(0)').height(),
            rightBlockSpacing = $('.rightBlock .article:eq(1)').offset().top - $('.rightBlock .article:eq(0)').offset().top - $('.rightBlock .article:eq(0)').height();

        if ((leftBlockHeight - rightBlockHeight) > 500 || rightBlockSpacing > 150) {
            $('.leftBlock').addClass('shorten');
            
            leftBlockHeight = $('.leftBlock').height();
            rightBlockHeight = $('.rightBlock').height();
        }
        
        if ((rightBlockHeight - leftBlockHeight) > 400 || leftBlockSpacing > 120) {
            $('.rightBlock').addClass('shorten');
            
            leftBlockHeight = $('.leftBlock').height(),
            rightBlockHeight = $('.rightBlock').height(),
            leftBlockSpacing = $('.leftBlock .article:eq(1)').offset().top - $('.leftBlock .article:eq(0)').offset().top - $('.leftBlock .article:eq(0)').height(),
            rightBlockSpacing = $('.rightBlock .article:eq(1)').offset().top - $('.rightBlock .article:eq(0)').offset().top - $('.rightBlock .article:eq(0)').height();
            
            if ((rightBlockHeight - leftBlockHeight) > 400 || leftBlockSpacing > 120) {
                $('.rightBlock').removeClass('shorten').addClass('shortenMore');
            }
        }
    }, 100);
}



function parsePage() {
    window.resizeDone = true;
    window.lastWidth = $(window).width();
    
    /* Moves captions into more friendly territory. */
    /* Surrounds gallery imgs with <div>s, as required by image slider */
    $('.slider img').wrap('<div class="sliderImageParent">');

    //$(".mainContent:not(:has(.slidecaption))").append('<span class="slidecaption"></span>');
    
    $('.aside img').each(function() {
        $(this).addClass('asideImg');
    });
    
    $('#articlesGroup > .article img, .topBlock .article img:not(.asideImg)').each(function() {
        $(this).attr("src", $(this).attr("src") + ".width=1000");
    });
    
    $(document).ready(function() {
        if (!$('.titlelist').is('.slick-slider')) {
            $('.titlelist').slick({
                infinite: true,
                slidesToShow: 6,
                slidesToScroll: 5,
                useTransform: false, /* Not working on Chrome right now. */
                useCSS: false,
                responsive: [
                    {
                        breakpoint: 980,    
                        settings: {
                            slidesToShow: 5,
                            slidesToScroll: 4,
                        }
                    },
                    {
                        breakpoint: 720,
                        settings: {
                            slidesToShow: 4,
                            slidesToScroll: 3
                        }
                    },
                    {
                        breakpoint: 600,
                        settings: {
                            slidesToShow: 3,
                            slidesToScroll: 2
                        }
                    },
                    {
                        breakpoint: 400,
                        settings: {
                            slidesToShow: 2,
                            slidesToScroll: 2
                        }
                    }
                ]
            });
        }

        $('.slider').each(function() {
            $(this).slickLightbox({
                itemSelector : " .sliderImageParent img ",
                src : function(e) {
                    return $(e).attr('src').replace(/\.width=.+$/, '') + '.width=1000';
                },
                caption : function(e) {
                    return $('.slidecaption', $(e).closest('.slider')).html();
                },
                captionPosition: 'bottom'
            }).on({
                // Reduces user confusion/distraction ("which set of buttons do I press?")
                'show.slickLightbox': function() {
                    $('.slick-slider:not(.slick-lightbox-slick) button').hide();
                },
                'hide.slickLightbox': function() {
                    $('.slick-slider:not(.slick-lightbox-slick) button').show();
                }
            })
        });

        $('.slider').each(function() {
            var t = this;

            $(this).on('init', function(event, slider) {
//                if (!$(this).closest('.slider').has('.slidecaption'))
                    $(this).closest('.slider').append($('<span class="slidecaption"></span>'));

                captionStuff(slider, $(this).closest('.slider'), 0);
            });

            $(this).slick({
                slickFilter : '.sliderImageParent',
                infinite: true,
                cssEase: 'linear',
                slidesToShow: 1,
                useTransform: false, // Not working on Chrome right now.
                useCSS: false,
                adaptiveHeight: true, // Good if you can use, but currently not working in Chrome. Like a lot of things.
                respondTo: 'slider'
            });

            // Otherwise their state becomes stuck on click, leading to a confusing user experience.
            $('.slick-next, .slick-prev', this).click(function() {
                this.blur();
            })

            /* Changing Caption Code */
            $(this).on('beforeChange', function(event, slider, currentSlide, nextSlide) {
                captionStuff(slider, $(this).closest('.slider'), nextSlide);
            });
        });

        function captionStuff(slider, slider0, slide) {
            var source = "";
            if (slider0.attr('data-source'))
                source = "Picture Credit: " + slider0.attr('data-source');
            else if ($(slider.$slides.get(slide)).children(0).attr('data-source'))
                source = "Picture Credit: " + $(slider.$slides.get(slide)).children(0).attr('data-source');


            var slideCaption = "";
            if (slider0.is('[data-caption]'))
                slideCaption = slider0.attr('data-caption');
            else if (slider0.is('[data-caption-usealt]'))
                slideCaption = $(slider.$slides.get(slide)).children(0).attr('alt');
            else if ($(slider.$slides.get(slide)).children(0).attr('data-caption'))
                slideCaption = $(slider.$slides.get(slide)).children(0).attr('data-caption');


            $('.slidecaption', slider0).html((slideCaption ? slideCaption + '<br />' : '') + (source ? '<small>' + source + '</small>' : ''));
        }
    });
    
    setTimeout(resizePage, 500);
}
$(document).ready(parsePage);
$("img").load(resizePage);
$(window).resize(resizePage);
