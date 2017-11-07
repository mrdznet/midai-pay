$(function () {
    pageInitModule.setWidth();
    pageInitModule.setSidebar();
    pageInitModule.setCarousel();
})
$(window).resize(function () {
    pageInitModule.setWidth();
})
$(window).scroll(function () {
    pageInitModule.setScrollToTop();
});
var imgErrorFnc = function(){
    
}


/*
* init page when page load
*/
var pageInitModule = (function (mod) {
    mod.setCarousel = function () {
        try {
            $('.carousel').hammer().on('swipeleft', function () {
                $(this).carousel('next');
            });
            $('.carousel').hammer().on('swiperight', function () {
                $(this).carousel('prev');
            });
        } catch (e) {
            // console.log("you mush import hammer.js and jquery.hammer.js to let the carousel can be touched on mobile");
        }
    };
    mod.setWidth = function () {
        if ($(window).width() < 768) {
            $(".sidebar").css({ left: -220 });
            $(".all").css({ marginLeft: 0 });
        } else {
            $(".sidebar").animate({ left: 0 });
            $(".all").animate({ marginLeft: 220 });
        }
    };
    mod.setScrollToTop = function () {
        var top = $(window).scrollTop();
        if (top < 60) {
            $('#goTop').hide();
        } else {
            $('#goTop').show();
        }
    };
    mod.setSidebar = function () {
        $('[data-target="sidebar"]').click(function () {
            var asideleft = $(".sidebar").offset().left;
            if (asideleft == 0) {
                $(".sidebar").animate({ left: -220 });
                $(".all").animate({ marginLeft: 0 });
                $(".navbar-fixed-top").animate({ left: 0 });
            }
            else {
                $(".sidebar").animate({ left: 0 });
                $(".all").animate({ marginLeft: 220 });
                $(".navbar-fixed-top").animate({ left: 218 });
            }
        });
        var toggleFlag = false; 
        var $prevSlide = null;
        var  $prevSlide = $(".home-menu>a");
        
        $(".has-sub>a").click(function () {
            // if(!$(this).hasClass('current')){
            //     $prevSlide.removeClass('current');
            //     $(this).addClass('current');
            // }
             if($prevSlide && $prevSlide.html() === $(this).html()){
                $(this).parent(".has-sub").toggleClass('current');
            }
            else{
                if($prevSlide){
                    $prevSlide.parent(".has-sub").removeClass("current");
                    $prevSlide.parent(".has-sub").find(".sub-menu").slideUp();
                }
                $(this).parent(".has-sub").addClass("current");
            }
            // $(this).parent(".has-sub").find(".sub-menu").slideToggle();
            $prevSlide = $(this);
           toggleFlag = true;
        })
    }
    return mod;
})(window.pageInitModule || {});
