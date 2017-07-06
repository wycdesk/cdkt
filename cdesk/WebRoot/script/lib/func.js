/**
 * Created by zhouzhongyu on 15/11/13.
 */

/**
 * 通知组件
 */
!function(){
    function _alert(msg,style){
        style = style || 'alert-success';
        var html = $('<div class="alert alert-dismissable '+ style+'">'+
        '    <button aria-hidden="true" data-dismiss="alert" class="close" type="button">×</button>'+
            msg+
        '</div>');
        var $alertBox = $('#alert-box');
        if(!$alertBox.length){
            $alertBox = $('<div id="alert-box" class="alert-box"></div>');
            $('body').append($alertBox);
        }
        $alertBox.append(html);
        window.setTimeout((function($d){
            return function(){
                $d.remove();
            };
        })(html),3*1000);
    }

    var Notice = function(){

    };

    Notice.prototype.alert = function(msg,style){
    	if(typeof goToLogin != 'undefined' && goToLogin instanceof Function){
    		goToLogin(msg);
    	}
        if(top.location != self.location){
            parent.window.notice.alert(msg,style);
        }
        else{
            _alert(msg,style);
        }
    };

    Notice.prototype.success = function(msg){
        this.alert(msg,'alert-success');
    };

    Notice.prototype.danger = function(msg){
        this.alert(msg,'alert-danger');
    };

    Notice.prototype.warning = function(msg){
        this.alert(msg,'alert-warning');
    };

    Notice.prototype.info= function(msg){
        this.alert(msg,'alert-info');
    };

    window.notice = new Notice();
}();

/**
 * Author zhouzy
 * Date   2014/9/23
 *
 * 青牛软件成都研发中心
 * 前端框架 cri 基础类
 *
 * 版本 v 2.0
 *
 */

!function(window){
    window.cri = {};

    function Class(){}

    Class.extend = function(subType){
        var prototype = (function(prototype){
            function Prototype(){}
            Prototype.prototype = prototype;
            return new Prototype();
        }(this.prototype));

        prototype.constructor = subType;

        subType.prototype = prototype;

        subType.extend = this.extend;

        return subType;
    };

    cri.Class = Class;

    /**
     * 获取表单值
     * @param $form
     * @returns {{}}
     */
    cri.getFormValue = function($form){
        var o = {},
            inputQuery = ":input:not(:button,[type=submit],[type=reset],[disabled])",
            selectQuery = "select";
        $(inputQuery,$form).each(function(){
            var role = $(this).attr('data-role');
            if(role && role == 'timeInput'){
                this.name && (o[this.name] = $(this).data("timeInput").getFormatValue());
            }
            else{
                this.name && (o[this.name] = $(this).val());
            }
        });
        $(selectQuery,$form).each(function(){
            var role = $(this).attr('data-role');
            if(role && role=='selectBox'){
                this.name && (o[this.name] = $(this).data("selectBox").value());
            }
            else{
                this.name && (o[this.name] = $(this).val());
            }
        });
        return o;
    };

    /**
     * 设置表单值
     * @param $form
     * @param o
     */
    cri.setFormValue = function($form,o){
        for(var name in o){
            var $i = $("[name=" + name + "]",$form);
            if($i.length){
                switch($i.attr('data-role')){
                    case 'input':{
                        $i.data('input').value(o[name]);
                    }break;
                    case 'timeInput':{
                        $i.data('timeInput').value(o[name]);
                    }break;
                    case 'selectBox':{
                        $i.data('selectBox').value(o[name]);
                    }break;
                    default:{
                        $i.val(o[name]);
                    }
                }
            }
        }
    };

    /**
     * 判断是否为闰年
     * @param year
     * @returns {boolean}
     */
    cri.isLeapYear = function(year){
        return (0==year%4&&((year%100!=0)||(year%400==0)));
    };

    /**
     * 日期格式化
     * 格式 YYYY/yyyy/YY/yy 表示年份
     * MM/M 月份
     * W/w 星期
     * dd/DD/d/D 日期
     * hh/HH/h/H 时间
     * mm/m 分钟
     * ss/SS/s/S 秒
     */
    cri.formatDate = function(date,formatStr){
        var str    = formatStr || 'yyyy-MM-dd',
            year   = date.getFullYear(),
            month  = date.getMonth()+1,
            day    = date.getDate(),
            hour   = date.getHours(),
            minute = date.getMinutes(),
            second = date.getSeconds();
        str=str.replace(/yyyy|YYYY/,year);str=str.replace(/yy|YY/,(year % 100)>9?(year % 100).toString():'0' + (year % 100));
        str=str.replace(/MM/,month>9?month.toString():'0' + month);str=str.replace(/M/g,month);
        str=str.replace(/dd|DD/,day>9?day.toString():'0' + day);str=str.replace(/d|D/g,day);
        str=str.replace(/hh|HH/,hour>9?hour.toString():'0' + hour);str=str.replace(/h|H/g,hour);
        str=str.replace(/mm/,minute>9?minute.toString():'0' + minute);str=str.replace(/m/g,minute);
        str=str.replace(/ss|SS/,second>9?second.toString():'0' + second);str=str.replace(/s|S/g,second);
        return str;
    };

    /**
     *把日期分割成数组
     */
    cri.date2Array = function(myDate){
        return [
            myDate.getFullYear(),
            myDate.getMonth(),
            myDate.getDate(),
            myDate.getHours(),
            myDate.getMinutes(),
            myDate.getSeconds()
        ];
    };

    /**
     * 日期保存为JSON
     * @param myDate
     * @returns {{yyyy: number, MM: number, dd: (*|number), HH: number, mm: number, ss: number, ww: number}}
     */
    cri.date2Json = function(myDate){
        return {
            yyyy:myDate.getFullYear(),
            MM:myDate.getMonth(),
            dd:myDate.getDate(),
            HH:myDate.getHours(),
            mm:myDate.getMinutes(),
            ss:myDate.getSeconds(),
            ww:myDate.getDay()
        };
    };

    /**
     * 字符串转成日期类型
     * 日期时间格式 YYYY/MM/dd HH:mm:ss  YYYY-MM-dd HH:mm:ss
     * 日期格式 YYYY/MM/dd YYYY-MM-dd
     */
    cri.string2Date = function(d){
        return new Date(Date.parse(d.replace(/-/g,"/")));
    };

    /**
     * 将json格式字符串转换成对象
     * @param json
     * @returns {*}
     *
     * {{deprecated}}
     */
    cri.parseJSON = function(json){
        return json ? (new Function("return " + json))(): {};
    };

    /**
     * 判断是否为数组
     * @param o
     * @returns {boolean}
     */
    cri.isArray = function(o){
        return Object.prototype.toString.call(o) === '[object Array]';
    };

    /**
     * 判断是否为数字
     * @param s
     * @returns {boolean}
     */
    cri.isNum = function(s){
        if (s!=null && s!=""){
            return !isNaN(s);
        }
        return false;
    };

    /**
     * 判断是否为电话号码
     * @param s
     * @returns {boolean}
     */
    cri.isPhoneNo = function(s){
        return /((\d{11})|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$)/.test(s);
    };

    /**
     * 判断是否为邮箱地址
     * @param s
     * @returns {boolean}
     */
    cri.isEmail = function(s){
        return /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))$/i.test(s);
    };
    /**
     * 拓展jQuery对象方法
     */
    !function(jQuery){
        /**
         * 获取元素的绝对高度像素值
         * 当父元素隐藏，或者父元素高度未设置时，返回null
         * 如果调用方法时，传入了value，则使用该value，不再去获取元素CSS高度及height属性
         * @param value 设定高度(String,Number,百分比)
         * @returns {*} 绝对高度值，类型为Number或者为Null
         * @private
         */
        $.fn._getHeightPixelValue = function(value){
            var $this       = $(this),
                styleHeight = $this[0].style.height,
                propHeight  = $this[0].height,
                calHeight   = value || styleHeight || propHeight;
            if(calHeight){
                //百分比
                if(/\d*%/.test(calHeight)){
                    var $parent = $this.parent();
                    if($parent.is(":visible") && $parent.css('height')!='0px'){
                        return Math.floor($this.parent().height() * parseInt(calHeight) / 100);
                    }
                    else{
                        return null;
                    }
                }
                else{
                    return parseInt(calHeight);
                }
            }
            else{
                return null;
            }
        };

        /**
         * 获取元素的绝对宽度像素值
         * 当父元素隐藏，或者父元素宽度未设置时，返回null
         * 如果调用方法时，传入了value，则使用该value，不再去获取元素CSS宽度及width属性
         * @param value 设定宽度(String,Number,百分比)
         * @returns {*} 绝对宽度值，类型为Number或者为Null
         * @private
         */
        $.fn._getWidthPixelValue = function(value){
            var $this       = $(this),
                styleWidth = $this[0].style.width,
                propWidth  = $this[0].width,
                calWidth   = value || styleWidth || propWidth;
            if(calWidth){
                //百分比
                if(/\d*%/.test(calWidth)){
                    var $parent = $this.parent();
                    if($parent.is(":visible") && $parent.css('width')!='0px'){
                        return Math.floor($this.parent().width() * parseInt(calWidth) / 100);
                    }
                    else{
                        return null;
                    }
                }
                else{
                    return parseInt(calWidth);
                }
            }
            else{
                return null;
            }
        };

        /**
         * 拓展 jquery formValue 方法
         *
         * @param o
         * @returns {{}}
         */
        $.fn.formValue = function(o){
            if(arguments.length>0){
                cri.setFormValue($(this),o);
            }
            else{
                return cri.getFormValue($(this));
            }
        };

        /**
         * 重置form
         */
        $.fn.formReset = function(param){
            var $this = $(this),
                inputQuery = ":input:not(:button,[type=submit],[type=reset],[disabled])",
                selectQuery = "select";
            param = param || {};
            $(inputQuery,$this).each(function(){
                var role = $(this).attr('data-role');
                var value = this.name ? param[this.name] : '';
                value = value || '';
                if(role){
                    if(role=='input'){
                        $(this).data("input").value(value);
                    }
                    else if(role == 'timeInput'){
                        value = value || new Date();
                        $(this).data("timeInput").value(value);
                    }
                }
                else{
                    $(this).val(value);
                }
            });
            $(selectQuery,$this).each(function(){
                var role = $(this).attr('data-role');
                var value = this.name ? param[this.name] : '';
                value = value || '';
                if(role && role=='selectBox'){
                    if(value != ''){
                        $(this).data("selectBox").value(value);
                    }
                    else{
                        $(this).data("selectBox").select(0);
                    }
                }
                else{
                    if(value != ''){
                        $(this).val(value);
                    }else{
                        this.selectedIndex = 0;
                    }
                }
            });
        };
    }(jQuery);
}(window);

/**
 * Author zhouzy
 * Date   2014/10/14
 *
 * jQuery 插件基类
 * 继承自Class
 *
 */
!function(window){

    "use strict";

    var cri = window.cri,
        $   = window.jQuery;

    var Widgets = cri.Class.extend(function(element,options){
        this.$element = $(element);
        this._initOptions(options);
        this._init();
        this._eventListen();
    });

    /**
     * 初始化组件配置参数
     * @param options
     * @private
     */
    Widgets.prototype._initOptions = function(options) {
        this.options = $.extend(true,{}, this.options, options);
    };

    /**
     * 初始化组件DOM结构
     * @private
     */
    Widgets.prototype._init = function(){};

    /**
     * 初始化组件监听事件
     * @private
     */
    Widgets.prototype._eventListen = function(){};

    /**
     * 销毁组件
     * @private
     */
    Widgets.prototype._destroy = function(){
        var $element = this.$element;
        var $warpper = $element.parent();
        $warpper.after($element).remove();
    };

    /**
     *
     * @private
     */
    Widgets.prototype._getHeightPixelValue = function($d,value){
        var styleHeight = $d[0].style.height,
            propHeight  = $d[0].height,
            calHeight   = value || styleHeight || propHeight;

        if(calHeight){
            var arr = ("" + calHeight).split("%");
            if(arr.length>1){
                /**
                 * 当 parent 为隐藏元素，则不能获取到parent高度,或者 parent 未设置高度,此时返回 null
                 */
                var $parent = $d.parent();
                if($parent.is(":visible") && $parent.style.height){
                    calHeight = Math.floor($d.parent().height() * arr[0] / 100);
                }
                else{
                    return null;
                }
                calHeight = (""+calHeight).split("px")[0];
            }
            if(calHeight){
                return parseInt(calHeight);
            }
        }
        else{
            return null;
        }
    };
    cri.Widgets = Widgets;
}(window);

/**
 * Author zhouzy
 * Date   2014/9/18
 * Pager 组件
 *
 */
!function(window){

    "use strict";

    var cri = window.cri,
        $   = window.jQuery;

    /**
     * 默认属性
     * @type {{page: number, pageSize: number, total: number}}
     * @private
     */
    var _defaultOptions = {
        page:1,      //当前页数
        pageSize:10, //每页条数
        total:0,     //总条数
        onPage:null,  //当用户点击翻页按钮时触发
        onUpdate:null //更新翻页信息结束触发
    };

    var FIRSTPAGE = "first-page",
        PREVPAGE  = "prev-page",
        NEXTPAGE  = "next-page",
        LASTPAGE  = "last-page",
        STATEDISABLED = "disabled",
        ACTIVE   = "active";

    var Pager = cri.Widgets.extend(function(element,options){
        this.options = _defaultOptions;
        this.pager = null;
        cri.Widgets.apply(this,arguments);
    });

    $.extend(Pager.prototype,{
        _eventListen:function(){
            var that = this;
            this.$pager.find('li').off('click');
            this.$pager.find('li:not(.'+STATEDISABLED+')').on("click",function(e){
                var $li = $(e.target).closest("li");
                var page = $li.data("page");
                that._page(page);
            });
        },

        _init:function () {
            this._createPager(this.$element);
        },

        _createPager:function($parent){
            var $pager   = this.$pager = $("<ul></ul>").addClass("pagination"),
                op       = this.options,
                pageSize = op.pageSize || 10,
                total    = op.total || 0,
                page     = parseInt(op.page) || 1,
                lastPage = Math.ceil(total / pageSize),

                $firstPage  = $('<li></li>').addClass(FIRSTPAGE).append('<a href="#"><span class="fa fa-angle-double-left"></span></a>'),
                $prevPage   = $('<li></li>').addClass(PREVPAGE).append('<a href="#"><span class="fa fa-angle-left"></span></a>'),
                $nextPage   = $('<li></li>').addClass(NEXTPAGE).append('<a href="#"><span class="fa fa-angle-right"></span></a>'),
                $lastPage   = $('<li></li>').addClass(LASTPAGE).append('<a href="#"><span class="fa fa-angle-double-right"></span></a>');

            lastPage = lastPage>0?lastPage:page;
            this._fourBtn($firstPage,$prevPage,$nextPage,$lastPage,page,lastPage);
            $pager.append($firstPage,$prevPage,$nextPage,$lastPage);
            $prevPage.after(this._numberPage(page,lastPage));
            $parent.append($pager);
        },

        _updatePagerBtn:function(){
            var op       = this.options,
                pageSize = op.pageSize || 10,
                total    = op.total || 0,
                page     = parseInt(op.page) || 1,
                lastPage = Math.ceil(total / pageSize),

                $firstPage  = $("." + FIRSTPAGE,this.$pager),
                $prevPage   = $("." + PREVPAGE,this.$pager),
                $nextPage   = $("." + NEXTPAGE,this.$pager),
                $lastPage   = $("." + LASTPAGE,this.$pager),

                $numberPage = $(".number",this.$pager);

            this._fourBtn($firstPage,$prevPage,$nextPage,$lastPage,page,lastPage);
            $numberPage.remove();
            $prevPage.after(this._numberPage(page,lastPage));
        },

        _fourBtn:function($firstPage,$prevPage,$nextPage,$lastPage,page,lastPage){
            var nextPage = page + 1,
                prevPage = page - 1;
            if(page <= 1){
                $firstPage.addClass(STATEDISABLED);
                $prevPage.addClass(STATEDISABLED);
            }
            else{
                $firstPage.removeClass(STATEDISABLED).data("page",1);
                $prevPage.removeClass(STATEDISABLED).data("page",prevPage);
            }

            if(page >= lastPage){
                $nextPage.addClass(STATEDISABLED);
                $lastPage.addClass(STATEDISABLED);
            }else{
                $nextPage.removeClass(STATEDISABLED).data("page",nextPage);
                $lastPage.removeClass(STATEDISABLED).data("page",lastPage);
            }
        },

        _numberPage:function(page,lastPage){
            var start = page > 2 ? page-2:1,
                end   = start + 4,
                numberPage = [];
            start = lastPage > 4 ? (end > lastPage ? lastPage - 4:start):start;
            for(var i=start; i<=end && i<=lastPage; i++){
                var $li = $('<li class="number"></li>').data("page",i),
                    $a  = $('<a href="javascript:void(0);"></a>').text(i);
                i != page ? $li.addClass("pager-num"): $li.addClass(ACTIVE);
                numberPage.push($li.append($a));
            }
            return numberPage;
        },

        _page:function(page){
            var op = this.options;
            op.page = page;
            this.options.onPage.call(this,op.page,op.pageSize);
        },

        update:function(page,pageSize,total){
            var op = this.options;
            op.total = total || op.total;
            op.page = page || op.page;
            op.pageSize = pageSize || op.pageSize;
            this._updatePagerBtn();
            this._eventListen();
            op.onUpdate && op.onUpdate(this);
        }
    });
    cri.Pager = Pager;
}(window);
/**
 * Author zhouzy
 * Date   2014/9/18
 * Button 组件
 *
 */
!function(window){

    "use strict";

    var cri = window.cri,
        $   = window.jQuery;

    var _defaultOptions = {
        text:"",
        iconCls:null,
        handler:null,
        enable:true
    };

    var BUTTON = "btn";

    var Button = cri.Widgets.extend(function(element,options){
        this.options     = _defaultOptions;
        this.$inputGroup = null;
        this.$button     = null;
        cri.Widgets.apply(this,arguments);
        this.$element.attr('data-role','button');
    });

    $.extend(Button.prototype,{
        _eventListen:function(){
            var that = this;
            this.$button.on("click",function(){
                that.options.enable && that.options.handler && that.options.handler.call();
            });
        },

        _init:function(){
            this._create();
        },

        _create:function(){
            var op = this.options,
                iconCls = op.iconCls || '',
                $e = this.$element.hide(),
                text = op.text || $e.text() || $e.val() || '',
                $icon = '<span class="icon"><i class="' + iconCls + '"></i></span>',
                buttonText = '<span class="text">'+text+'</span>';

            $e.wrap('<div class="'+ BUTTON + '"></div>');
            this.$button = $e.parent();
            this.$button.append($icon, buttonText);
            if(!op.enable){
                this.disable();
            }
        },

        /**
         * 设置按钮文本值
         * @param text
         */
        text:function(text){
            this.$button.find('text').text(text);
        },

        /**
         * 设置按钮图标
         * @param className
         */
        iconCls:function(className){
            this.$button.find('.icon i').attr('class',className);
        },

        enable:function(){
            this.$button.removeClass("disabled");
            this.options.enable = true;
        },

        disable:function(){
            this.$button.addClass("disabled");
            this.options.enable = false;
        }
    });

    cri.Button = Button;

    $.fn.button = function(option) {
        var o = null;
        this.each(function () {
            var $this   = $(this),
                button  = $this.data('button'),
                options = typeof option == 'object' && option;
            if(button != null){
                button._destroy();
            }
            $this.data('button', (o = new Button(this, options)));
        });
        return o;
    };
}(window);
/**
 * Author zhouzy
 * Date   2014/9/18
 * grid 组件
 * include Pager
 */
!function(window){

    "use strict";

    var cri = window.cri,
        $   = window.jQuery;

    /**
     * 定义表格标题，工具栏，分页高度
     */
    var _titleH       = 31, //标题高度
        _toolbarH     = 31, //工具栏高度
        _pagerH       = 41, //分页高度
        _gridHeadH    = 31, //表格头高度
        _cellMinW     = 5;  //单元格最小宽度

    function _getPlainText(text){
        text = ("" + text).replace(/(<.*?>)|(<\/.*?>)/g,"");
        return text;
    }

    /**
     * 获取Grid每列信息
     * @param $table        原始 table jquery对象
     * @param optionColumns 使用 options.columns 初始化列属性
     * @returns {*}         处理后的列属性
     * @private
     */
    function _getColumnsDef($table,optionColumns){
        var columns = optionColumns || (function(){
                var columns = [];
                $("tr th,td", $table).each(function(){
                    var options = '{' + $(this).data("options") + '}';
                    columns.push($.extend({title:$(this).html()},cri.parseJSON(options)));
                });
                return columns;
            }());

        $.map(columns,function(column){
            if(column.title && column.width){
                column._width = column.width;
            }
            return column;
        });

        return columns;
    }

    /**
     * 表格默认属性
     * @private
     */
    var _defaultOptions = {
        title:null,
        height:null,
        width:null,
        toolbar:null,
        url:null,
        param:{},
        checkBox:false,
        rowNum:true,
        columns:null,
        pagination:true,
        page:1,
        pageSize:10,
        filter:false,

        onChange:null,   //行点击时触发
        onSelected:null, //当选择一行或者多行时触发
        onDblClick:null, //双击行时触发
        onLoad:null,     //构造表格结束时触发
        ajaxDone:null   //当AJAX请求成功后触发
    };

    var Grid = cri.Widgets.extend(function(element,options){
        this.options     = _defaultOptions;
        this.$element    = $(element);
        this.$grid       = null;
        this.$gridhead   = null;
        this.$gridbody   = null;
        this.toolbar     = null;
        this.pager       = null;
        this.$title      = null;
        this._rows       = null;
        this._columns    = [];
        this._selectedId = [];
        this._gridClassName = this._gridClassName || "datagrid";
        cri.Widgets.apply(this,arguments);
    });

    $.extend(Grid.prototype,{
        /**
         * 监听用户事件
         * @private
         */
        _eventListen:function(){
            var that       = this,
                op         = this.options,
                dragStartX = 0,
                clickTimer = null;

            this.$gridbody
                .on("scroll",function(){
                    $(".grid-head-wrap",that.$gridhead).scrollLeft($(this).scrollLeft());
                })
                .on('click', "tr", function(e){
                    if(clickTimer != null){
                        clearTimeout(clickTimer);
                    }
                    clickTimer = window.setTimeout(function(){
                        $("input[type=checkbox]",that.$gridhead).prop("checked",false);
                        that._setSelected(e);
                        var item  = $(e.target).closest("tr"),
                            rowId = item.data('rowid'),
                            row = that._getRowDataById(rowId);
                        that.options.onChange && that.options.onChange.call(that,row);
                    },300);
                })
                .on('dblclick', "tr", function(e){
                    clearTimeout(clickTimer);
                    that._onDblClickRow(e);
                });
            $(document).on("mouseup",function(e){
                that.$gridhead.css("cursor","");
                $(document).off("mousemove");
            });

            this.$gridhead
                .on('mousedown',".drag-line",function(e){
                    that.$gridhead.css("cursor","e-resize");
                    var dragLineIndex = 0;
                    op.rowNum && dragLineIndex++;
                    op.checkBox && dragLineIndex++;
                    dragLineIndex += $(this).data("drag");
                    var $col = $("col:eq("+ dragLineIndex +")",that.$gridhead);
                    dragStartX = e.pageX;
                    $(document).on("mousemove",function(e){
                        var px = e.pageX - dragStartX;
                        var width = +$col.width() + px;
                        var tableWidth = $("table",that.$gridhead).width();
                        dragStartX = e.pageX;
                        if(width >= _cellMinW){
                            $("table",that.$gridbody).width(tableWidth + px);
                            $("table",that.$gridhead).width(tableWidth + px);
                            $("col:eq("+ dragLineIndex +")",that.$gridhead).width(width);
                            $("col:eq("+ dragLineIndex +")",that.$gridbody).width(width);
                        }
                    });
                })
                .on('click',"input[type=checkbox]",function(e){
                    var isChecked = $(e.target).prop("checked");
                    if(isChecked){
                        that._selectedId = [];
                        $("tr",that.$gridbody).each(function(){
                            var $tr = $(this);
                            that._selectedId.push($tr.data("rowid"));
                            $tr.addClass("selected");
                            $('input[type=checkbox]',$tr).prop("checked",isChecked);
                        });
                    }else{
                        that._selectedId = [];
                        $("tr",that.$gridbody).removeClass("selected");
                    }
                    $("input[type=checkbox]",that.$gridbody).each(function(){
                        $(this).prop("checked",isChecked);
                    });
                    op.onChange && op.onChange.call(that);
                });
        },

        /**
         * 初始化组件HTML结构
         * @private
         */
        _init:function() {
            this._columns = _getColumnsDef(this.$element,this.options.columns);
            this._createGrid();
            this._createPage();
            this._getData();
            if(this.options.onLoad && typeof(this.options.onLoad) === 'function'){
                this.options.onLoad.call(this);
            }
            this._colsWidth();
        },

        /**
         * 初始化表格HTML结构
         * @private
         */
        _createGrid:function(){
            var height = this.$element._getHeightPixelValue(this.options.height);
            var width  = this.$element._getWidthPixelValue(this.options.width);
            var $grid  = $("<div></div>").addClass("grid").addClass(this._gridClassName);
            height && (height-=2);//减去border
            width && (width-=2);
            $grid.attr("style",this.$element.attr("style"))
                .css({width:width,height:height,display:'block'});
            this.$element.wrap($grid);
            this.$element.hide();
            this.$grid = this.$element.parent();
            this._createTitle(this.$grid);
            this._createToolbar(this.$grid);
            this._createGridView(this.$grid,height);
        },

        /**
         * 初始化表格 View HTML 结构
         * @param $parent
         * @param height
         * @private
         */
        _createGridView:function($parent,height){
            this.$gridview = $("<div></div>").addClass("grid-view");
            this.$gridhead = $("<div></div>").addClass("grid-head");
            $parent.append(this.$gridview.append(this.$gridhead).append(this.$gridbody));
            if(height){
                height -= _gridHeadH;
                this.options.title      && (height -= _titleH);
                this.options.toolbar    && (height -= _toolbarH);
                this.options.pagination && (height -= _pagerH);
            }
            this._createHead(this.$gridhead);
            this.$gridbody = this._createBody(height);
            this.$grid.append(this.$gridbody);
        },

        /**
         * 初始化表格标题 HTML 结构
         * @param $grid
         * @private
         */
        _createTitle:function($grid){
            if(this.options.title){
                this.$title = $('<div class="title"><span>' + this.options.title + '</span></div>');
                $grid.append(this.$title);
            }
        },

        /**
         * 初始化表格头 HTML 结构
         * @param $parent
         * @private
         */
        _createHead:function($parent){
            var $headWrap = $("<div></div>").addClass("grid-head-wrap"),
                $table    = $('<table class="table"></table>'),
                $tr       = $("<tr></tr>"),
                op        = this.options,
                columns   = this._columns;

            $table.append(this._createColGroup($parent.width()));

            if(op.checkBox){
                var $lineCheckbox = $("<td></td>").addClass("line-checkbox").append('<span class="td-content"><input type="checkbox"/></span>');
                $tr.append($lineCheckbox);
            }
            if(op.rowNum){
                var $lineNumber = $("<td></td>").addClass("line-number").append('<div class="td-content"></div>');
                $tr.append($lineNumber);
            }

            for(var i = 0,len = columns.length; i<len; i++){
                var that = this,
                    $td        = $('<td></td>'),
                    $dragLine  = $('<div></div>').addClass('drag-line').data('drag',i),
                    $tdContent = $('<div></div>').addClass('td-content grid-header-text'),
                    column     = columns[i];
                for(var key in column){
                    var value = column[key];
                    if(key == 'title'){
                        $tdContent.prop('title',value).append(value);
                        if(op.filter){
                            var $filterIcon = $('<span data-for="'+column.field+'" class="fa fa-filter filter-icon"></span>');
                            $filterIcon.click(function(e){
                                var gridHeadOffset = that.$grid.offset();
                                var iconOffset = $(this).offset();
                                var offset = {
                                    left:iconOffset.left-gridHeadOffset.left+10,
                                    top:iconOffset.top-gridHeadOffset.top
                                };
                                var key = $(e.target).data('for');
                                that._toggle(that.$grid.find('ul[data-for='+key+']'),offset);
                            });
                            $tdContent.append($filterIcon);
                        }
                    }
                    else if(key == 'style'){
                        $td.css(value);
                    }
                }

                $td.append($tdContent);
                i < (len - 1) && $td.append($dragLine);
                $tr.append($td);
            }
            $table.append($tr);
            $parent.html($headWrap.html($table));
        },

        /**
         * 初始化表格数据部分 HTML 结构
         * @param gridBodyHeight
         * @returns {*|HTMLElement}
         * @private
         */
        _createBody:function(gridBodyHeight){
            var $gridbody = $('<div class="grid-body loading"></div>'),
                $loadingIcon = $('<i class="fa fa-spinner fa-spin"></i>').addClass("loadingIcon");
            gridBodyHeight && $gridbody.height(gridBodyHeight);
            $gridbody.append($loadingIcon);
            return $gridbody;
        },

        /**
         *
         * 刷新Grid Body数据行
         * @private
         */
        _refreshBody:function(rows){
            var $table   = $('<table class="table"></table>'),
                op       = this.options,
                id       = 0,
                lineNum  = 1 + op.pageSize * (op.page - 1),
                columns  = this._columns;
            rows = rows || this._rows;
            this._selectedId = [];
            $table.append($("colgroup",this.$gridhead).clone());
            for(var i = 0,len = rows.length; i<len; i++){
                var row = rows[i],
                    $tr = $('<tr></tr>').data("rowid",id);
                if(op.checkBox){
                    var $checkBoxTd = $('<td class="line-checkbox"></td>');
                    if(row.check){
                        $tr.append($checkBoxTd.append('<input type="checkbox" checked/>'));
                        $tr.addClass("selected");
                        this._selectedId.push(id);
                    }else{
                        $tr.append($checkBoxTd.append('<input type="checkbox"/>'));
                    }
                }
                if(op.rowNum){
                    $tr.append($("<td></td>").addClass("line-number").append(lineNum));
                }
                for(var j = 0,length = columns.length; j<length;j++){
                    var $td = $('<td></td>'),
                        $content = $('<div></div>').addClass('td-content'),
                        column = columns[j],
                        text   = row[column.field]==null ? "" : row[column.field],
                        _text  = ("" + text).replace(/(<([^a\/]).*?>)|(<\/[^a].*?>)/g,"");
                    if(column['button']){
                        var button = column.button;
                        $content.append('<button></button>');
                        $('button',$content).button(button);
                    }else{
                        $content.prop("title",_text).append(_text);
                    }
                    $tr.append($td.append($content));
                }
                lineNum++;id++;
                $table.append($tr);
            }
            this.$gridbody.removeClass("loading").html($table);

            /**
             * 根据gird-body纵向滚动条宽度决定headWrap rightPadding
             * 当grid-body为空时，在IE下不能取到clientWidth
             */
            var clientWidth = this.$gridbody.prop("clientWidth");
            if(clientWidth){
                var scrollBarW = this.$gridbody.width()-clientWidth;
                this.$gridhead.css("paddingRight",scrollBarW);
            }
        },

        /**
         * 生成 cols HTML 结构
         * @param parentWidth
         * @returns {*|HTMLElement}
         * @private
         */
        _createColGroup:function(parentWidth){
            var $cols   = [],
                op      = this.options,
                columns = this._columns;
            op.checkBox && $cols.push($("<col/>").width(30));
            op.rowNum   && $cols.push($("<col/>").width(25));
            for(var i = 0,len = columns.length; i<len; i++){
                var $col = $("<col/>");
                columns[i]._width && $col.width(columns[i]._width);
                $cols.push($col);
            }
            return $("<colgroup></colgroup>").append($cols);
        },

        /**
         * 生成工具栏 HTML 结构
         * @param $parent
         * @private
         */
        _createToolbar:function($parent){
            if(this.options.toolbar) {
                this.toolbar = new cri.ToolBar($parent, {
                    buttons: this.options.toolbar
                });
            }
        },

        /**
         * 初始化表头过滤器
         * @returns {{}}
         * @private
         */
        _initFilter:function(){
            var rows = this._rows;
            var column = this._columns;
            var keysMap = {};
            this._filters = this._filters || {};

            for(var i in column){
                var field = column[i].field;
                var keys = {};
                for(var j=0,len=rows.length;j<len;j++){
                    var row = rows[j];
                    var value = row[field];
                    if(keys[value] == undefined){
                        keys[value] = true;
                    }
                }
                keysMap[field] = keys;
            }
            this._createFilterLists(keysMap);
        },

        _createFilterLists:function(keysMap){
            var that =this;
            for(var key in keysMap){
                var gridHeadOffset = this.$grid.offset();
                var filterIconOffset = this.$gridhead.find('span[data-for='+key+'].filter-icon').offset();
                var offset = {
                    left:filterIconOffset.left-gridHeadOffset.left+10,
                    top:filterIconOffset.top-gridHeadOffset.top
                };
                var $ul = that.$grid.find('ul[data-for='+key+']');
                $ul.length ? $ul.empty() : ($ul=$('<ul class="grid-filter" data-for="'+key+'"></ul>'));

                var $all = $('<input type="checkbox" name="all"/>').click(function(e){
                    var $ul = $(e.target).closest('ul');
                    if($(e.target).prop('checked')){
                        $ul.find('input[type=checkbox]').prop("checked",true);
                    }
                    else{
                        $ul.find('input[type=checkbox]').prop("checked",false);
                    }
                });
                $ul.append($('<li></li>').append($all,'全部'));

                var map = keysMap[key];
                for(var i in map){
                    var plainText = _getPlainText(i);
                    plainText=='null' && (plainText = '');
                    var $checkbox = $('<input type="checkbox" name="'+plainText+'"/>').data("value",i);
                    $ul.append($('<li class="filter-value"></li>').append($checkbox,plainText));
                }
                var $footer = $('<li class="footer"><button class="cancel">过滤</button></li>');
                $ul.append($footer);
                $ul.css(offset);
                $footer.find('button').button({
                    handler:(function($ul){
                        return function(){
                            var field = $ul.attr('data-for');
                            var values = [];
                            $ul.find('li.filter-value input[type=checkbox]').each(function(){
                                if($(this).prop('checked')){
                                    values.push($(this).data('value'));
                                }
                            });
                            that._filters[field] = values;
                            that._filter();
                            that._toggle($ul);
                        }
                    }($ul))
                });

                this.$grid.append($ul);

                $ul.find('li.filter-value input[type=checkbox]').click(function($all){
                    return function(){
                        $all.prop('checked',false)
                    };
                }($all));
            }
        },

        /**
         * 显示隐藏过滤器下拉选择框
         * @param $filterList
         * @private
         */
        _toggle:function($filterList,offset){
            var that = this;
            if($filterList.is(":hidden")){
                $filterList.css(offset).slideDown(200, function(){
                    $(document).mouseup(function(e) {
                        var _con = $filterList;
                        if (!_con.is(e.target) && _con.has(e.target).length === 0) {
                            $filterList.slideUp(200);
                        }
                    });
                });
            }
            else{
                $filterList.slideUp(200);
            }
        },

        /**
         * 执行过滤
         * @private
         */
        _filter:function(){
            var filters = this._filters;
            var rows = this._rows;

            for(var field in filters){
                var values = filters[field];
                var tempRows = [];
                for(var i= 0,len=values.length; i<len; i++){
                    var value = values[i];
                    for(var j=0,jLen = rows.length;j<jLen;j++){
                        if(("" + rows[j][field]) == value){
                            tempRows.push(rows[j]);
                        }
                    }
                }
                rows = tempRows;
            }
            this._refreshBody(rows);
        },

        /**
         * 生成翻页 HTML 结构
         * @private
         */
        _createPage:function(){
            var op = this.options;
            var grid = this;
            if(this.options.pagination){
                this.pager = new cri.Pager(this.$grid,{
                    page:op.page,
                    pageSize:op.pageSize,
                    total:0,
                    rowsLen:0,
                    onPage:function(page,pageSize){
                        op.page = page;
                        op.pageSize = pageSize;
                        grid._getData();
                    }
                });
            }
        },

        /**
         * 获取数据
         * @returns {boolean}
         * @private
         */
        _getData:function(){
            var result = true,
                op     = this.options,
                that   = this;
            if(op.pagination){
                op.param.page = op.page;
                op.param.rows = op.pageSize;
            }
            $.ajax({
                type: "post",
                url: this.options.url,
                success: function(data){
                    if(op.ajaxDone){
                        var re = op.ajaxDone.call(that,data);
                        re && (data = re);
                    }
                    that._rows = data.rows || [];
                    op.total = data.total || 0;
                    that.pager && that.pager.update(op.page,op.pageSize,op.total,that._rows.length);
                    $('input[type=checkbox]',that.$gridhead).prop("checked",false);
                    that._refreshBody();
                    if(op.filter){
                        that._initFilter();
                    }
                },
                error: function(){
                    that._rows = [];
                    op.total = 0;
                    that.pager && that.pager.update(op.page,op.pageSize,op.total,that._rows.length);
                    that._refreshBody();
                },
                data:op.param,
                dataType:"JSON",
                async:false
            });
            return result;
        },

        /**
         * 当用户点击选中行时触发onSelected事件
         * 当options.checkbox=true为多选,否则为单选
         * @param e
         * @private
         */
        _setSelected:function(e){
            var item  = $(e.target).closest("tr"),
                rowId = item.data('rowid');

            if(!this.options.checkBox){
                if(item.hasClass("selected")){
                    item.removeClass("selected");
                    this._selectedId = [];
                }else{
                    $("tr.selected",this.$gridbody).removeClass("selected");
                    item.addClass("selected");
                    this._selectedId = [rowId];
                }
            }
            else{
                if(item.hasClass("selected")){
                    var index = $.inArray(rowId,this._selectedId);
                    index >= 0 && this._selectedId.splice(index,1);
                    item.removeClass("selected");
                    $("input[type=checkbox]",item).prop("checked",false);
                }else{
                    this._selectedId = this._selectedId || [];
                    item.addClass("selected");
                    this._selectedId.push(rowId);
                    if(this.options.checkBox){
                        $("input[type=checkbox]",item).prop("checked",true);
                    }
                }
            }
            if(this._selectedId && this._selectedId.length){
                this.options.onSelected && this.options.onSelected.call(this);
            }
        },

        /**
         * 根据td计算col的宽度
         * @private
         */
        _colsWidth:function(){
            var that = this;
            $("tr:eq(0) td",this.$gridhead).each(function(index){
                $('col:eq('+ index +')',that.$gridbody).width($(this).width() + 2*4);
                $('col:eq('+ index +')',that.$gridhead).width($(this).width() + 2*4);
            });
        },

        /**
         * 根据 rowid 获取某一行的数据
         * @param rowid
         * @returns {*}
         * @private
         */
        _getRowDataById:function(rowid){
            return this._rows[parseInt(rowid)];
        },

        /**
         * 当用户双击某一行触发
         * @param e
         * @private
         */
        _onDblClickRow:function(e){
            var that  = this,
                op    = this.options,
                item  = $(e.target).closest("tr"),
                rowid = item.data('rowid');
            this._selectedId = [rowid];
            $("tr.selected",this.$gridbody).removeClass("selected");
            item.addClass("selected");
            if(this.options.checkBox){
                $("input[type=checkbox]",this.$gridbody).prop("checked",false);
                $("input[type=checkbox]",item).prop("checked",true);
            }
            op.onDblClick && op.onDblClick.call(that,that._getRowDataById(rowid));
        },

        /**
         * 根据 param 重新加载表格
         * @param param
         */
        reload:function(param){
            param && (this.options.param = param);
            this.options.page = 1;
            this._selectedId = [];
            this._getData();
        },

        /**
         * 根据 param 指定的数据加载表格
         * @param param
         */
        loadData:function(param){
            if(param.push){
                this._rows = param;
                this._refreshBody();
            }
        },

        /**
         * 获取用户选择的数据
         * @returns {Array}
         */
        getSelected:function(){
            var selected = [],
                selectedId = this._selectedId;
            for(var i=0; i<selectedId.length;i++){
                selected.push(this._getRowDataById(selectedId[i]));
            }
            return selected;
        }

    });

    cri.Grid = Grid;
}(window);

/**
 * Author zhouzy
 * Date   2014/9/23
 *
 * TreeGrid
 *
 * 依赖 Grid
 */
!function(window){

    var $   = window.jQuery,
        cri = window.cri;

    var TreeGrid = cri.TreeGrid = cri.Grid.extend(function(element,options){
        this._gridClassName = "treegrid";
        cri.Grid.apply(this,arguments);
        this._selfEvent();
        this.$element.attr('data-role','treegrid');
    });

    TreeGrid.prototype._selfEvent = function(){
        var that = this;
        this.$gridbody
            .on('click', "tr td.line-file-icon i", function(e){
                that._fold(e);e.preventDefault();
            });
    };

    TreeGrid.prototype._refreshBody = function(){
        var $parent = this.$gridbody,
            $table  = $('<table class="table"></table>'),
            op      = this.options,
            columns = this._columns,
            lineNum = 1,
            paddingLeft = 1,
            that = this,
            iconWidth = 6;

        $table.append(this._createColGroup($parent.width()));
        /**
         * 拼装每行HTML
         */
        this._selectedId = [];
        !function getRowHtml(rows,isShow,id){
            for(var i = 0,len = rows.length; i<len; i++){
                var $tr = $("<tr></tr>").data("rowid",++id).attr("data-rowid",id);
                var row = rows[i];

                isShow == "show" || $tr.hide();

                if(op.checkBox){
                    if(row.check){
                        $tr.addClass("selected");
                        $tr.append($("<td></td>").addClass("line-checkbox").append('<input type="checkbox" checked/>'));
                        that._selectedId.push(id);
                    }else{
                        $tr.append($("<td></td>").addClass("line-checkbox").append('<input type="checkbox"/>'));
                    }
                }
                if(op.rowNum){
                    $tr.append($("<td></td>").addClass("line-number").append(lineNum++));
                }

                getColHtml($tr,columns,row,paddingLeft);
                $table.append($tr);

                if(row.children && row.children.length > 0){
                    row.hasChildren = true;
                    paddingLeft += iconWidth;
                    row.state && row.state == "closed" ?
                        getRowHtml(row.children,"hide",id*1000) :
                        getRowHtml(row.children,isShow,id*1000);
                    paddingLeft -= iconWidth;
                }
            }
        }(this._rows,"show",0);

        /**
         * 拼装列HTML
         * @param colDef     列定义
         * @param colData    列数据
         * @param textIndent 文件图标缩进
         * @param nodeId     id
         * @returns {Array}
         */
        function getColHtml($tr,colDef,colData,textIndent){
            var fileIcons = {"file":"fa fa-file","folderOpen":"fa fa-folder-open","folderClose":"fa fa-folder"};

            $.each(colDef,function(index){
                var $td = $("<td></td>");
                var text = colData[this.field]==null ? "" : colData[this.field];

                if(this.field == "text"){
                    var $icon = $("<i></i>").attr("class",fileIcons.file);
                    if(colData.hasChildren || (colData.children && colData.children.length)){
                        colData.state == "closed" ? $icon.attr("class",fileIcons.folderClose):$icon.attr("class",fileIcons.folderOpen);
                    }
                    $td.css("text-indent",textIndent).addClass("line-file-icon").append($icon).append(text);
                }
                else if(this.field == "button"){
                    var button = this.button;
                    if(cri.isArray(button)){
                        for(var b in button){
                            var $b = $('<button></button>');
                            $td.append($b);
                            var o = button[b];
                            $b.button({
                                text: o.text,
                                iconCls: o.iconCls,
                                handler:function(o){
                                    return function(){
                                        o.handler(colData);
                                    };
                                }(o)
                            });
                        }
                    }
                    else{
                        $td.append('<button></button>');
                        $('button',$td).button({
                            text: button.text,
                            iconCls: button.iconCls,
                            handler:function(){
                                button.handler(colData);
                            }
                        });
                    }
                }
                else{
                    $td.text(text);
                }
                $tr.append($td);
            });
        }
        this.$gridbody.removeClass("loading").html($table);
        //根据gird-body 纵向滚动条决定headWrap rightPadding
        var scrollBarW = $parent.width()-$parent.prop("clientWidth");
        this.$gridhead.css("paddingRight",scrollBarW);
    };

    TreeGrid.prototype._fold = function(e){
        var op    = this.options,
            item  = $(e.target).closest("tr"),
            rowid = item.data('rowid'),
            that  = this;
        this.selectedRow = this._getRowDataById(rowid);

        if(this.selectedRow.state == "closed") {
            this.selectedRow.state = "open";
            if(op.async && !this.selectedRow.children){
                var pa = {};
                $.each(this.selectedRow,function(index,data){index != "children" && (pa[index] = data);});
                this.selectedRow.childrenList || $.ajax({
                    type: "post",
                    url: op.asyncUrl,
                    success: function(data, textStatus){
                        that.selectedRow.children = data.rows;
                    },
                    data:pa,
                    dataType:"JSON",
                    async:false
                });
            }
        }
        else{
            this.selectedRow.state = "closed";
        }
        this._refreshBody();
    };

    TreeGrid.prototype._checkbox = function(e){
        var input = $(e.target),
            tr    = $(e.target).closest("tr"),
            rowid = parseInt(tr.data('rowid')),
            id    = rowid,
            row   = this._getRowDataById(rowid),
            isChecked = input.prop("checked");

        !function(data){
            if(data.children && data.children.length > 0){
                id *= 1000;
                !function ita(data){
                    $.each(data,function(){
                        id += 1;
                        $("tr[data-rowid="+ id +"] input[type=checkbox]").prop("checked",isChecked);
                        if(this.children && this.children.length > 0){
                            id*=1000;
                            ita(this.children);
                            id= Math.floor(id/=1000);
                        }
                    });
                }(data.children);
            }
        }(row);
    };

    TreeGrid.prototype._getRowDataById = function(rowid){
        var op = this.options
            ,rowdata = null;
        rowid = parseInt(rowid);

        !function getRow(data){
            var arr = [];
            while(rowid >= 1){
                var t = rowid%1000;
                arr.push(t);
                rowid = Math.floor(rowid/1000);
            }
            for(var i = arr.length - 1; i >= 0 ; i--){
                var k = arr[i] - 1;
                data[k]&&(rowdata = data[k])&&(data = data[k].children);
            }
        }(this._rows);
        return rowdata;
    };

    $.fn.treegrid = function(option) {
        var treeGrid = null;
        this.each(function () {
            var $this    = $(this),
                options  = typeof option == 'object' && option;
            treeGrid = $this.data('treegrid');
            treeGrid && treeGrid.$grid.before($this).remove();
            $this.data('treegrid', (treeGrid = new TreeGrid(this, options)));
        });
        return treeGrid;
    };
}(window);

$(document).ajaxStart(function() {
    var $loading = $(".loading-box");
    if($loading.length == 0) {
        $loading = $(
            '    <div class="loading-box">' +
            '    <div class="sk-spinner sk-spinner-three-bounce">' +
            '        <div class="sk-bounce1"></div>' +
            '        <div class="sk-bounce2"></div>' +
            '        <div class="sk-bounce3"></div>' +
            '    </div>' +
            '</div>'
        );
        $('body').append($loading);
    }
    $loading.show();
});

$(document).ajaxComplete(function(){
    $(".loading-box").hide();
});

$(function(){
    $.material.init();
});

Handlebars.registerHelper('equal', function(v1, v2, options) {
    if(v1 === v2) {
        return options.fn(this);
    }
    return options.inverse(this);
});
Handlebars.registerHelper('formatDate', function(date,formatStr) {
    return cri.formatDate(new Date(date),formatStr);
});

