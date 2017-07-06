 //tooltip

    /*冒泡提示*/
    function tip() {
        var js_tip = $(".tooltip");
        js_tip.hover(function() {
            var text = $(this).attr("title");
            var w = $(this).innerWidth();
            var h = $(this).innerHeight();
            var x = $(this).offset().left;
            var y = $(this).offset().top;
            $(this).attr("title", "");
            var $tip_main = $("<div class='tip-main'></div>").appendTo("body").html("<i></i>" + text);
            var t_w = $tip_main.innerWidth();
            $tip_main.fadeIn(400).css({
                left: x - (t_w / 2) + (w / 2),
                top: h + y + 10
            });
        }, function() {
            var text = $(".tip-main:last").text(); //注意是最后一个对象的原因
            $(this).attr("title", text);
            $(".tip-main").fadeOut(100, function() {
                $(this).remove();
            });
        });
    }
    tip()



    function tooltip() {
        $('.js-tooltip').tooltipster({
            contentAsHTML: true,
            interactive: true,
            //theme: '.my-custom-theme',
            position: 'bottom',
            maxWidth: 320,
            autoClose: true
        });
        // $('.tooltip-sm').tooltipster({
        //     //animation: 'grow',
        //     contentAsHTML: true,
        //     interactive: true,
        //     theme: '.tooltip-sm-theme',
        //     position: 'bottom',
        //     maxWidth: 320,
        //     autoClose:true
        // });
        // $('.tooltip-ticket').tooltipster({
        //     contentAsHTML: true,
        //     interactive: true,
        //     theme: '.tooltip-ticket-theme',
        //     position: 'bottom',
        //     autoClose:true,
        //     arrowColor: '#F9FAFA'
        // });
    }
    tooltip();