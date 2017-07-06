<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>联络历史管理首页</title>
    <%@include file="/views/include/pageHeader.jsp"%>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/script/lib/dropzone/dropzone.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/innerFrame.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/usermanage.css">
	<style>
		#formId{
			width:560px;
			
		}
		#formId .form-group{
			width:45%;
			margin-right:15px;
			display:inline-block;
		}
		#userFieldUl .form-group{
			vertical-align: top;
		}
		.ulcenter{
			text-align:center;
		}
	</style>
</head>
<body>
<div id="left-part">
    <header class="part-header">
        <div class="sidebar">联络历史管理</div>
    </header>
    <div class="left-content" id="leftContent">
        <div class="left-content-panel">
            <div class="left-content-panel-header">联络历史<a onclick="refreshAll()">刷新</a><a onclick="openAddCH()">+新增联络历史</a></div>
            <ul class="left-content-panel-body left-part-list" id="allHistory">
                <li data-ch-id="1"><a>ceshi</a></li>
                <c:forEach items="${historyList}" var="item">
                        <li data-ch-id="${item.id}"><a>${item.title}</a></li>
                </c:forEach>
            </ul>
        </div>
        <div class="left-content-panel">
            <div class="left-content-panel-header">联络历史自定义视图<a onclick="addView()">+新增视图</a></div>
            <ul class="left-content-panel-body left-part-list" id="customizeHistory"></ul>
        </div>
        <div class="left-content-panel">
            <div class="left-content-panel-header">自定义字段</div>
            <ul class="left-content-panel-body left-part-list" id="customizeField"></ul>
        </div>
    </div>
</div>
<div id="right-part">
    <header class="part-header">
        <span>最新用户</span>
    </header>

    <div class="right-content" id="rightContainer">
        <iframe name="iframe" id="rightIframe" style="display:none" width="100%" height="100%" src="" frameborder="0" data-id="index_v1.html" seamless></iframe>
        <div class="right-content-panel container" id="rightDiv" >
            <div class="table-content">
                <div class="col-12 grid" style="overflow-x:auto;">
                    <table class="table" cellspacing="0" cellpadding="0" id="grid" style="table-layout:fixed;">
                        <thead>
                        <tr class="order" id="chTableTitle">
                            <th width="40"><input class="ember-view ember-checkbox all-checkbox" type="checkbox" id="allSelect"> </th>
                            <th width="80">客户</th>
                            <th width="100">联络方式</th>
                            <th width="100">业务类型</th>
                            <th width="100">关联工单</th>
                            <th width="100">联络小结</th>
                            <th width="100">创建人</th>
                            <th width="100">创建时间</th>
                            <th width="100">自定义字段</th>
                        </tr>
                        </thead>
                        <tbody></tbody>
                        <tfoot>
                            <tr id="paginationTR">
                                <td colspan="6"><div id="pagination"></div></td>
                            </tr>
                        </tfoot>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <div id="toolbar">
        <button id="deleteBtn" type="button" class="btn btn-raised left-btn btn-danger">删除</button>
        <button id="cancelBtn" type="button" class="btn btn-raised btn-default btn-dark" onclick="cancel()">取消</button>
        <button id="editBtn" type="button" class="btn btn-raised btn-primary" data-toggle="modal" data-target="#editUserModal">编辑</button>
    </div>
</div>

<script id="customize-history-tpl" type="text/x-handlebars-template">
    {{#each customizeHistoryList}}
    <li data-ch-id="{{this.id}}"><a>{{this.title}}</a></li>
    {{/each}}
</script>

<script id="customize-field-tpl" type="text/x-handlebars-template">
    {{#each tempTitleList}}
    <li data-ch-id="{{this.tempId}}"><a>{{this.title}}</a></li>
    {{/each}}
</script>

<script id="table-ch-tpl" type="text/x-handlebars-template">
    <tr>
        <input type="checkbox">
        <td data-prop="userName">{{userName}}</td>
        <td data-prop="contactType">{{contactType}}</td>
        <td data-prop="businessType">{{businessType}}</td>
        <td data-prop="associationTicket">{{associationTicket}}</td>
        <td data-prop="contactTag">{{contactTag}}</td>
        <td data-prop="creater">{{creater}}</td>
        <td data-prop="createTime">{{createTime}}</td>
    </tr>
</script>

<script>
        // 基础url
    var BASE_CH_URL = "<%=request.getContextPath()%>/history/queryCommHistory?sessionKey=" + $.cookie("sessionKey"),
        // 当前所选联络历史的id
        curCHId = 0;

    $(function(){
        // 注册标题点击事件
        $("#leftContent").on('click', 'li', function(event, isRefresh){
            curCHId = $(this).data("chId");
            $("#leftContent .left-content-panel li").removeClass("active");
            $(this).addClass("active");
            // 修改右边表格标题
            $("#right-part .part-header > span").text($(this).find("a").text());

            refresh(isRefresh);
        });

        // 触发click事件，进入后默认显示联络历史的第一栏
        $("#allHistory li:first-child").trigger('click', [true]);
    });

    /*
    * @desc 刷新所有数据
    * @author Lesty
    * @codeDate 2016.5.27
    * */
    function refreshAll() {
        refresh(true);

        // 触发click事件，进入后默认显示联络历史的第一栏
        $("#allHistory li:first-child").trigger('click', [true]);
    }

    /*
    * @desc 新增自定义视图
    * */
    function addView() {
        //parent.openTab("<%=request.getContextPath()%>/history/addView", null, "新增联络历史", null);
        console.log('addView');
    }

    /*
    * @desc 打开新增联络历史页面
    * @author Lesty
    * @codeDate 2016.5.24
    * */
    function openAddCH() {
        var url = "<%=request.getContextPath()%>/history/add",
            title = "新增联络历史";

        parent.openTab(url,null,title,null);
    }

    /**
     * 向后台请求数据
     */
    var getTableData = (function(){
            // 保存请求参数
        var paramCache = {};
        /**
         * 初始化分页
         */
        var pager = new cri.Pager($("#pagination"),{
            page:1,
            pageSize:10,
            total:0,
            onPage:function(page,pageSize){
                $.extend(paramCache, {page: page, rows: pageSize});
                getTableData(paramCache);
            }
        });

        return function(param, isRefresh) {
            $.post(BASE_CH_URL, param, function(data){
                if(data.rows == null || data.rows.length === 0){
                    notice.alert("暂无数据！");
                    $("#allSelect").hide();
                } else {
                    createTable(data.rows);

                    if(isRefresh === true) {
                        refreshTitle();
                    }

                    pager.update(param.page, param.rows, data.total);
                    $.extend(paramCache, param);
                }
            });
        }
    })();

    /**
     * 刷新表格
     */
    var createTable = (function(){
        var tableCHTpl = Handlebars.compile($("#table-ch-tpl").html()),
            $table = $("#grid");

        return function(tableData){

            var html = [], // 获得数据后的所有行模板
                fns = null, // 表格所要展示的列名
                $tr = null, // 获得数据后的行模板
                checkCount = tableData.length; // 表格数据长度

            for(var i= 0,len = tableData.length; i < len; i++){

                // 获取行数据
                $tr = $(tableCHTpl(tableData[i]));

                html.push($tr);
            }

            // 替换数据
            $table.find("tbody").empty().append(html);

            // 绑定选择框事件(全选和单选)
            $table.find("input[type=checkbox]").change(function(){
                var $t = $(this).parent(),
                    checkLen = 0; // 状态为选中的按钮数量

                // 如果点击的是全选按钮, 则修改当前页所有选择框
                if($t.is("th")){
                    $table.find("td input[type=checkbox]").prop("checked", $(this).prop("checked"));
                    // 获取状态为选中的按钮数量
                    checkLen = $table.find('td input[type=checkbox]:checked').length;
                } else {
                    // 获取状态为选中的按钮数量
                    checkLen = $table.find('td input[type=checkbox]:checked').length;
                    // 如果所有数据都被选中，则让全选按钮状态为true
                    if(checkCount == checkLen){
                        $("#allSelect").prop("checked",true);
                    }
                    else{
                        $("#allSelect").prop("checked",false);
                    }
                }

                // 如果有选择数据，则显示工具栏
                if(checkLen > 0){
                    $("#editBtn").text("编辑 "+ checkLen +" 个用户");
                    $("#toolbar").addClass("show");
                }
                else{
                    $("#toolbar").removeClass("show");
                }
            });
        };
    })();

    /*
    * @desc 删除行按钮点击事件
    * */
    $("#deleteBtn").click(function (){

    });

    /* 取消按钮 */
    function cancel(){
        $("#toolbar").removeClass("show");
    }

    /*
    * @desc 刷新页面数据(表格/左侧侧边栏)
    * @author Lesty
    * @codeDate 2016.5.27
    * @isRefresh Boolean [是否刷新左侧侧边栏，默认为不刷新 true: 刷新]
    * */
    function refresh(isRefresh) {
        getTableData({page:1, rows:10, entId:'${entId}', id: curCHId}, isRefresh);
    }

    /*
    * @desc 刷新左侧自定义数据
    * @author Lesty
    * @codeDate 2016.5.26
    * @data Object [包含标题编译模板所需数据的对象]
    * */
    var refreshTitle = (function() {
            // 自定义视图标题模板
        var chTitleTpl = Handlebars.compile($("#customize-history-tpl").html()),
            // 自定义字段标题模板
            cfTitleTpl = Handlebars.compile($("#customize-field-tpl").html()),
            $customizeField = $("#customizeField"),
            $customizeHistory = $("#customizeHistory");

        return function(data) {
            $customizeHistory.empty().append($(chTitleTpl(data)));
            $customizeField.empty().append($(cfTitleTpl(data)));
        };
    })();
</script>
</body>
</html>