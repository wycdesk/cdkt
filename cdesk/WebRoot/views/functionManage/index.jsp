<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>功能管理首页</title>
    <%@include file="/views/include/pageHeader.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/innerFrame.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/functionManage.css">
<style type="text/css">
	.left-part-list li a:hover {
    background: #eff4f5 none repeat scroll 0 0;
}
</style>
</head>
<body>
<div id="left-part">
    <header class="part-header">
        功能管理
    </header>
    <div class="left-content">
        <div class="left-content-panel">
            <ul class="left-content-panel-body left-part-list">
                <li class="active" id="attachment"><a>附件</a></li>
                <li id="userfields"><a>用户自定义字段</a></li>              
                <li id="workTemplates"><a>工单自定义分类</a></li>
                <li id="commHistoryFields"><a>联络历史自定义字段</a></li>
            </ul>
        </div>
    </div>
    <div id="right-part">
        <div class="panel-wrap" style="display:block;">
            <h2>附件管理</h2>
            <nav class="navbar navbar-default">
                <!-- Nav tabs -->
                <ul class="nav navbar-nav" role="tablist">
                    <li role="presentation" class="active">
                        <a href="#whole" aria-controls="whole" role="tab" data-toggle="tab" aria-expanded="true"
                           onclick="showPage('3','#allAttachment','#pagination_1','#Allattachment-template')">全部</a>
                    </li>
                    <li role="presentation">
                        <a href="#job-enclosure" aria-controls="job-enclosure" role="tab" data-toggle="tab"
                           aria-expanded="false"
                           onclick="showPage('0','#wfAttachment','#pagination_2','#Wfattachment-template')">工单附件</a>
                    </li>
                    <li role="presentation">
                        <a href="#unused-enclosure" aria-controls="unused-enclosure" role="tab" data-toggle="tab"
                           aria-expanded="false"
                           onclick="showPage('','#ElseWf','#pagination_3','#Elseattachment-template')">未使用的附件</a>
                    </li>
                </ul>
            </nav>
            <!-- Tab panes -->
            <div class="tab-content">
                <!-- 全部 -->
                <div id="whole" class="tab-pane active" role="tabpanel">
                    <div class="whole-screen">
                        <div>
                            <select class="attachmentType">
                                <option selected="selected" value="4">所有格式</option>
                                <option value="1">图片</option>
                                <option value="2">文档</option>
                                <option value="3">压缩包</option>
                                <option value="0">表格</option>
                            </select>
                        </div>
                        <div class="fr">
                            <a class="btn-sm red" onclick="goDelete()">删除选中附件</a>
                        </div>
                    </div>
                    <div class="whole-message">
                        <table class="table-ui1" id="allAttachment">
                            <thead>
                            <tr>
                                <th class="table-col-1">附件信息</th>
                                <th class="table-col-2">上传用户</th>
                                <th class="table-col-3 ar">
                                    <input class="delete-all" type="checkbox" name="" style="margin:0 20px 0 0;">
                                </th>
                            </tr>
                            </thead>

                            <tbody>
                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="7">
                                    <div id="pagination_1"></div>
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
                <!-- 工单附件 -->
                <div id="job-enclosure" class="tab-pane" role="tabpanel">
                    <div class="whole-screen">
                        <div>
                            <select class="attachmentType">
                                <option selected="selected" value="4">所有格式</option>
                                <option value="1">图片</option>
                                <option value="2">文档</option>
                                <option value="3">压缩包</option>
                                <option value="0">表格</option>
                            </select>
                        </div>
                        <div class="fr">
                            <a class="btn-sm red" onclick="goDelete()">删除选中附件</a>
                        </div>
                    </div>
                    <div class="whole-message">
                        <table class="table-ui1" id="wfAttachment">
                            <thead>
                            <tr>
                                <th class="table-col-1">附件信息</th>
                                <th class="table-col-2">上传用户</th>
                                <th class="table-col-3 ar">
                                    <input class="delete-all" type="checkbox" style="margin:0 20px 0 0;"/>
                                </th>
                            </tr>
                            </thead>
                            <tbody>

                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="7">
                                    <div id="pagination_2"></div>
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
                <!-- 未使用的附件 -->
                <div id="unused-enclosure" class="tab-pane" role="tabpanel">
                    <div class="whole-screen">
                        <div>
                            <select class="attachmentType">
                                <option selected="selected" value="4">所有格式</option>
                                <option value="1">图片</option>
                                <option value="2">文档</option>
                                <option value="3">压缩包</option>
                                <option value="0">表格</option>
                            </select>
                        </div>
                        <div class="fr">
                            <a class="btn-sm red" onclick="goDelete()">删除选中附件</a>
                        </div>
                    </div>
                    <div class="whole-message">
                        <table class="table-ui1" id="ElseWf">
                            <thead>
                            <tr>
                                <th class="table-col-1">附件信息</th>
                                <th class="table-col-2">上传用户</th>
                                <th class="table-col-3 ar">
                                    <input class="delete-all" type="checkbox" name="" style="margin:0 20px 0 0;"/>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="7" style="position: relative;">
                                    <div id="pagination_3"></div>
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <iframe style="width:100%;height:100%;border:0;display:none;" ></iframe>

    </div>
</div>

<div id="preview" class="modal fade bs-example-modal-sm" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <span class="modal-title">预览</span>
            </div>
            <div class="modal-body" id="addPicture">

            </div>
        </div>
    </div>
</div>

<script src="<%=request.getContextPath()%>/H+3.2/js/plugins/footable/footable.all.min.js"></script>

<script id="Allattachment-template" type="text/x-handlebars-template">
    <tr>
        <td>
            <span class="file orange0"><i class="fa fa-file-powerpoint-o"></i></span>

            <p class="file-name">{{originalName}}<span>({{size}}K)</span></p>

            <p>
                {{#equal source "0"}}
                工单
                {{else}}
                文档
                {{/equal}}
                <a>
                    {{#if sourceId}}
                    # {{sourceId}} {{sourceTitle}}
                    {{/if}}</a></p>
        </td>
        <td>
            <p class="author">
                {{# if userName}}
                <a>{{userName}}</a>
                {{else}}
                <a>用户已删除</a>
                {{/if}}</p>
            <time>{{createTime}}</time>
        </td>
        <td>
            {{#equal type "1"}}
            <a class="btn-sm" data-toggle="modal" data-path="{{fileNew}}" data-target="#preview"
               onclick='addPicture(this)'>预览</a>
            {{/equal}}
            <a class="btn-sm" href="<%=request.getContextPath()%>/attachments/download?newFileName={{fileNew}}">下载</a>
            <input type="checkbox" style="float:right;top:6px;margin:0 20px 0 0;">
        </td>
    </tr>
</script>

<script id="Wfattachment-template" type="text/x-handlebars-template">
    <tr>
        <td>
            <span class="file orange0"><i class="fa fa-file-powerpoint-o"></i></span>

            <p class="file-name">{{originalName}}<span>({{size}}K)</span></p>

            <p>
                工单
                <a>
                    {{#if sourceId}}
                    # {{sourceId}} {{sourceTitle}}
                    {{/if}}</a>
            </p>
        </td>
        <td>
            <p class="author">
                {{# if userName}}
                <a>{{userName}}</a>
                {{else}}
                <a>用户已删除</a>
                {{/if}}</p>
            <time>{{createTime}}</time>
        </td>
        <td>
            {{#equal type "1"}}
            <a class="btn-sm" data-toggle="modal" data-path="{{fileNew}}" data-target="#preview"
               onclick='addPicture(this)'>预览</a>
            {{/equal}}
            <a class="btn-sm" href="<%=request.getContextPath()%>/attachments/download?newFileName={{fileNew}}">下载</a>
            <input type="checkbox" style="float:right;top:6px;margin:0 20px 0 0;">
        </td>
    </tr>
</script>

<script id="Elseattachment-template" type="text/x-handlebars-template">
    <tr>
        <td>
            <span class="file orange0"><i class="fa fa-file-powerpoint-o"></i></span>

            <p class="file-name">{{originalName}}<span>({{size}}K)</span></p>

            <p>文档<a>
                {{#if sourceId}}
                # {{sourceId}} {{sourceTitle}}
                {{/if}}</a></p>
        </td>
        <td>
            <p class="author">
                {{# if userName}}
                <a>{{userName}}</a>
                {{else}}
                <a>用户已删除</a>
                {{/if}}
            </p>
            <time>{{createTime}}</time>
        </td>
        <td>
            {{#equal type "1"}}
            <a class="btn-sm" data-toggle="modal" data-path="{{fileNew}}" data-target="#preview"
               onclick='addPicture(this)'>预览</a>
            {{/equal}}
            <a class="btn-sm" href="<%=request.getContextPath()%>/attachments/download?newFileName={{fileNew}}">下载</a>
            <input type="checkbox" style="float:right;top:6px;margin:0 20px 0 0;">
        </td>
    </tr>
</script>
<script>
    $(document).ready(function () {
        $('body').data("tfoot", "#pagination_1");
        $('body').data("tableId", "#allAttachment");
        $('body').data("scriptTemplate", "#Allattachment-template");
        getTableAllData({page: 1, rows: 5, source: "3", type: "4"});

        //初始化事件
        $(".attachmentType").change(function () {
            param = {};
            param.type = $(this).val();
            switch ($('body').data("tfoot")) {
                case "#pagination_1":
                    param.source = "3";
                    $.extend(param, {page: 1, rows: 5});
                    getTableAllData(param);
                    break;
                case "#pagination_2":
                    param.source = "0";
                    $.extend(param, {page: 1, rows: 5});
                    getTableWfData(param);
                    break;
                case "#pagination_3":
                    param.source = "";
                    $.extend(param, {page: 1, rows: 5});
                    getTableUnuseData(param);
                    break;
            }

        });
        //点击左列表处理
        $(".left-part-list li").click(function(){
        	$(".left-part-list li.active").removeClass("active");
        	$(this).addClass("active");
        	if($(this).attr("id")=="userfields"){
        		$("iframe").attr("src","<%=request.getContextPath()%>/userField/userfields");
        		$(".panel-wrap").css("display","none");
        		$("iframe").css("display","");
        	}
        	if($(this).attr("id")=="attachment"){
        		$(".panel-wrap").css("display","block");
        		$("iframe").css("display","none");
        	}
        	
        	if($(this).attr("id")=="workTemplates"){
        		$("iframe").attr("src","<%=request.getContextPath()%>/workTemplate/workTemplates");
        		$(".panel-wrap").css("display","none");
        		$("iframe").css("display","");
        	}
        	if($(this).attr("id")=="commHistoryFields"){
        		$("iframe").attr("src","<%=request.getContextPath()%>/commHistoryTemplate/commHistoryTemplates");
        		$(".panel-wrap").css("display","none");
        		$("iframe").css("display","");
        	}
        	
        });
    });

    //根据类型刷新数据
    function showPage(type, tableId, tfoot, template){
        $('body').data("tableId", tableId);
        $('body').data("scriptTemplate", template);
        $('body').data("tfoot", tfoot);
        switch ($('body').data("tfoot")) {
            case "#pagination_1":
                getTableAllData({page: 1, rows: 5, source: "3", type: "4"});
                $(".attachmentType").val("4");
                break;
            case "#pagination_2":
                getTableWfData({page: 1, rows: 5, source: "0", type: "4"});
                $(".attachmentType").val("4");
                break;
            case "#pagination_3":
                getTableUnuseData({page: 1, rows: 5, source: "", type: "4"});
                $(".attachmentType").val("4");
                break;
        }

    }
    /**
     * 向后台请求数据
     */
    var getTableAllData = (function () {

        var paramCache = {};

        /**
         * 初始化分页
         */
        var pager = new cri.Pager($("#pagination_1"), {
            page: 1,
            pageSize: 5,
            total: 0,
            onPage: function (page, pageSize) {
                $.extend(paramCache, {page: page, rows: pageSize});
                getTableAllData(paramCache);
            }
        });

        return function (param) {

            $.ajax({
                url: "<%=request.getContextPath()%>/attachments/query",
                dataType: 'json',
                data: param,
                success: function (data) {
                    console.dir(data);
                    $("input[type=checkbox]").prop("checked", false);
                    if (data.success) {
                        createTable(data.rows);
                        pager.update(param.page, param.rows, data.total);
                        $.extend(paramCache, param);
                    }
                    else {
                        notice.alert('请求数据失败', 'alert-danger');
                    }
                }
            });
        }
    })();


    var getTableWfData = (function () {

        var paramCache = {};

        /**
         * 初始化分页
         */
        var pager = new cri.Pager($("#pagination_2"), {
            page: 1,
            pageSize: 5,
            total: 0,
            onPage: function (page, pageSize) {
                $.extend(paramCache, {page: page, rows: pageSize});
                getTableWfData(paramCache);
            }
        });

        return function (param) {

            $.ajax({
                url: "<%=request.getContextPath()%>/attachments/query",
                dataType: 'json',
                data: param,
                success: function (data) {
                    console.dir(data);
                    $("input[type=checkbox]").prop("checked", false);
                    if (data.success) {
                        createTable(data.rows);
                        pager.update(param.page, param.rows, data.total);
                        $.extend(paramCache, param);
                    }
                    else {
                        notice.alert('请求数据失败', 'alert-danger');
                    }
                }
            });
        }
    })();

    var getTableUnuseData = (function () {
        var paramCache = {};
        /**
         * 初始化分页
         */
        var pager = new cri.Pager($("#pagination_3"), {
            page: 1,
            pageSize: 5,
            total: 0,
            onPage: function (page, pageSize) {
                $.extend(paramCache, {page: page, rows: pageSize});
                getTableUnuseData(paramCache);
            }
        });

        return function (param) {
            $.ajax({
                url: "<%=request.getContextPath()%>/attachments/query",
                dataType: 'json',
                data: param,
                success: function (data) {
                    console.dir(data);
                    $("input[type=checkbox]").prop("checked", false);
                    if (data.success) {
                        createTable(data.rows);
                        pager.update(param.page, param.rows, data.total);
                        $.extend(paramCache, param);
                    }
                    else {
                        notice.alert('请求数据失败', 'alert-danger');
                    }
                }
            });
        }
    })();

    /**
     * 刷新所有附件表格
     */
    var createTable = (function () {
        return function (tableData) {
            var trTemp = Handlebars.compile($($('body').data("scriptTemplate")).html());
            var $table = $($('body').data("tableId"));
            var html = [];
            for (var i = 0; i <= tableData.length-1; i++) {
                if (tableData[i].createTime) {
                    tableData[i].createTime = cri.formatDate(new Date(tableData[i].createTime), "yyyy年MM月dd日 HH:mm:ss");
                }
                var size = tableData[i].size / 1024;
                tableData[i].size = Math.round(size * 100) / 100;
                var $tr = $(trTemp(tableData[i]));

                $tr.data("row", tableData[i]);
                html.push($tr);
            }
            $table.find("tbody").empty().append(html);
            $table.find("thead input[type=checkbox]").change(function () {
                if ($(this).prop("checked")) {
                    $("input[type=checkbox]").prop("checked", true);
                } else {
                    $("input[type=checkbox]").prop("checked", false);
                }
            });
        };
    })();

    //动态添加图片预览事件
    function addPicture(e) {
        $pictureAddr = $("<img style='max-width:560px;'></img>");
        var basepath = "../attachments/${entId}/";
        var picture = basepath + $(e).data("path");
        //添加图片地址
        $pictureAddr.attr("src", picture);
        $("#addPicture").empty().append($pictureAddr);
    }

    //添加附件删除功能
    function goDelete() {
        var data = getSelected();
        if (data.length == 0) {
            notice.alert("请选择至少一条记录！", "alert-danger");
            return;
        }
        var param = {};
        param.newFileName = data.join(",");
        $.post("<%=request.getContextPath()%>/attachments/delete", param, function (data) {
            if (data.success) {
                notice.alert(" 删除成功！", "alert-success");
                $(".attachmentType").change();
            } else {
                notice.alert("删除失败！", "alert-danger");
            }
        });
    }

    function getSelected() {
        var $tableId = $($('body').data("tableId"));
        var $tablebody = $tableId.find("tbody");
        var data = [];
        $tablebody.find("input[type=checkbox]").each(function () {
            if ($(this).prop("checked")) {
                data.push($(this).closest("tr").data("row").fileNew);
            }
        });
        return data;
    }
</script>
</body>
</html>