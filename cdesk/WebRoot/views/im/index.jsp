<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link href="<%=request.getContextPath()%>/static/css/IMWindow.css" rel="stylesheet" type="text/css">
<div class="container-fluid" id="IMWindowContainer" style="display:none;">
    <div class="left-container">
        <div class="panel">
            <div id="im-container"></div>
        </div>
    </div>
    <div class="right-container">
        <div class="panel customerContainer">
            <div class="panel-heading">未查询任何客户信息</div>
        </div>
    </div>
</div>
<script id="right-container-template" type="text/x-handlebars-template">
    <div class="panel customerContainer">
        <ul class="nav nav-tabs" role="tablist">
            <li role="presentation" class="active"><a href="[data-id=client-data]" aria-controls="client-data" role="tab" data-toggle="tab">客户资料</a></li>
            <li role="presentation"><a href="[data-id=client-order]" aria-controls="client-order" role="tab" data-toggle="tab">工单</a></li>
            <li role="presentation"><a href="[data-id=client-history]" aria-controls="client-history" role="tab" data-toggle="tab">联络历史</a></li>
            <li role="presentation"><a href="[data-id=client-knowledge]" aria-controls="client-knowledge" role="tab" data-toggle="tab">知识库</a></li>
        </ul>
        <div class="tab-content">
            <div role="tabpanel" class="tab-pane active" data-id="client-data">
                <div class="client-data">
                    <div class="form-group">
                        <ul>
                            <li><label>姓名：</label><input placeholder="-" type="text" size="30" data-name="userName" value="{{userName}}"></li>
                            <li><label>手机：</label><input placeholder="-" type="text" size="30" data-name="telPhone" value="{{telPhone}}"></li>
                            <li><label>座机：</label><input placeholder="-" type="text" size="30" data-name="fixedPhone" value="{{fixedPhone}}"></li>
                            <li><label>邮箱：</label><input placeholder="-" type="text" size="30" data-name="email" value="{{email}}"></li>
                            <li><label>签名：</label><input placeholder="-" type="text" size="30" data-name="signature" value="{{signature}}"></li>
                            <li><label>详细信息：</label><input placeholder="-" type="text" size="30" data-name="remark" value="{{remark}}"></li>
                        </ul>
                    </div>
                    <div class="form-group">
                        <header class="client-heading">相关客户：</header>
                        <ul data-id="relationUser">
                            
                        </ul>
                    </div>
                </div>
            </div>
            <div role="tabpanel" class="tab-pane " data-id="client-order">
                <div class="client-order">
                    <div class="col-12 grid">
                        <a class="set-up btn btn-primary" data-id="createWorkButten">创建</a>
                        <table data-id="latelyOrderGrid" class="table footable" cellspacing="0" cellpadding="0" data-page-size="10">
                            <thead>
                                <tr class="order"><th>标题</th><th>发起人</th><th>创建时间</th></tr>
                            </thead>
                            <tbody></tbody>
                            <tfoot>
                            <tr>
                                <td colspan="3">
                                    <div class="pagination-ul" data-id="paginationLately"></div>
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
            <div role="tabpanel" class="tab-pane" data-id="client-history">
                <div class="client-history">
                    <div class="col-12 grid">
                        <table data-id="commGrid" class="table footable" cellspacing="0" cellpadding="0" data-page-size="10">
                            <thead>
                            <tr class="order">
                                <th style="width:21%;">姓名</th>
                                <th style="width:21%;">渠道</th>
                                <th style="width:20%;">客服</th>
                                <th>联络时间</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="4">
                                    <div class="pagination-ul" data-id="paginationComm"></div>
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
            <div role="tabpanel" class="tab-pane" data-id="client-knowledge">
                <div class="client-knowledge">
                    <div class="col-12 grid">
                        <table class="tabel" data-id="knowledgeTable" cellspacing="0" cellpadding="0" data-page-size="10">
                            <thead>
                            <tr><th>标题</th><th style="width:20%">作者</th><th style="width:36%">创建日期</th></tr>
                            </thead>
                            <tbody></tbody>
                            <tfoot>
                            <tr>
                                <td colspan="3"><div class="pagination-ul" data-id="paginationKnowledge"></div></td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</script>
<script id="table-tr-template" type="text/x-handlebars-template">
    <tr id="commHistory">
        <td data-prop="userName">{{userName}}</td>
        <td data-prop="source">{{source}}</td>
        <td data-prop="source">{{opName}}</td>
        <td data-prop="createTimeStr">{{createTime}}</td>
    </tr>
</script>
<!-- 最近工单模板 -->
<script id="table-tr-template-lately" type="text/x-handlebars-template">
    <tr class="ember-view">
        <td class="title"><a>{{title}}</a></td>
        <td>{{customName}}</td>
        <td>{{createDate}}</td>
    </tr>
</script>
<!-- 工单悬浮框模板 -->
<script id="tooltip-template" type="text/x-handlebars-template">
    <div class="tip-content panel panel-default">
        <div class="tip-header panel-heading">
            <span class="col-12"># {{workId}}</span><span class="tip-order-state">{{statusStr}}</span>
        </div>
        <div class="tip-row panel-body">
            <div class="container-fluid">
                <div class="row">
                    <span class="col-xs-3">标题</span><span class="col-xs-9">{{title}}</span>
                </div>
                <div class="row">
                    <span class="col-xs-3">时间</span><span class="col-xs-9">{{createDate}}</span>
                </div>
                <div class="row">
                    <span class="col-xs-3">发起人</span><span class="col-xs-9">{{customName}}</span>
                </div>
            </div>
        </div>
    </div>
</script>
<!-- 知识库模板 -->
<script id="table-tr-template-knowledge" type="text/x-handlebars-template">
    <tr class="ember-view">
        <td class="title"><a href="javascript:void(0);">{{title}}</a></td>
        <td>{{author}}</td>
        <td>{{createTime}}</td>
    </tr>
</script>
<!-- 关联客户模板 -->
<script id="relation-template" type="text/x-handlebars-template">
    <li>
        <span>{{this.userName}}</span><span>{{this.telPhone}}</span>
    </li>
</script>
<%@include file="customerInfo.jsp"%>
