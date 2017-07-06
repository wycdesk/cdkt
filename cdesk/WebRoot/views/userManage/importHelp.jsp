<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>如何批量导入用户</title>
    <%@include file="/views/include/pageHeader.jsp"%>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/normalize.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/enter.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/importHelp.css">
</head>

<body>
<header class="head">
    <div class="wrap hader-inner">
        <div class="logo">
                <img src="<%=request.getContextPath()%>/static/images/logo.png" />
            	<span  id="entInfo" >
				</span>
        </div>
        <!-- <nav class="header-navigation">
            <ul>
                <li>
                    <a>产品</a>
                </li>
                <li>
                    <a>价格</a>
                </li>
                <li>
                    <a>客户</a>
                </li>
                <li class="active">
                    <a>帮助</a>
                </li>
                <li>
                    <a>开发者</a>
                </li>
                <li>
                    <a>App</a>
                </li>
                <li>
                    <a>资源</a>
                </li>
                <li class="bn orange">
                    <a>20秒免费开通服务</a>
                </li>
            </ul>
        </nav> -->
    </div>
</header>
<div class="header">
    <div class="wrap hader-inner">
       <!--  <nav class="header-nav">
            <ul class="header-nav-list clearfix">
                <li><a href="">帮助中心首页</a></li>
                <li><a href="">开发者讨论</a></li>
                <li><a href="">更新日志</a></li>
                <li><a href="">讨论社区</a></li>
                <li><a href="">提交新问题</a></li>
            </ul>
            <div class="user-nav">
                <div class="user-info">
                    <div class="dropdown-toggle">
                        <span>
                            <a>登录</a>
                        </span>
                    </div>
                </div>
            </div>
        </nav> -->
    </div>
</div>
<main>
    <header class="main-header">
        <!-- <div class="wrap">
            <ul class="breadcrumbs">
                <li> <a href="">首页</a></li>
                <li> <a href="">#新手指引#</a></li>
                <li> <a href="">基础知识</a></li>
            </ul>
            <form class="main-header-search" role="search" method="get">
                <label>
                    <input placeholder="输入问题关键字，找到答案" type="search">
                </label>
            </form>
        </div> -->
    </header>
    <section class="content">
        <div class="wrap">
            <section class="content-main">
                <div>
                    <header class="article-header">
                        <h2>如何批量导入用户</h2>
                        <p class="article-header-info">
                            <span class="author">【青牛CDESK】  qnsoft</span>·
                            <span>发表于：2016年02月15日  下午  3:53</span>·
                            <span>更新于：2016年02月15日  下午  3:53</span>
                        </p>
                    </header>
                    <div class="article-content">
                        <div class="original-content">
                            <p>青牛CDESK允许您通过使用规范的文件格式标准，将您的客户和客户服务职员信息批量导入到帮助台系统中，免去单个添加人员的不便</p>
                            <p> <br> </p>
                            <p><strong>导入文件标准：</strong></p>
                            <p>导入的文件必须为CSV格式的文件，且字符编码为UTF-8格式</p>
                            <p> <br> </p>
                            <p><strong>导入用户和客服职员</strong></p>
                            <p><strong>导入文件撰写规范和标准</strong></p>
                            <p><span style="text-decoration:underline;">CSV文件的第一行填写列名</span></p>
                            <p>列名为以下项目：</p>
                            <table class="content_table" cellspacing="0" cellpadding="0">
                                <tbody>
                                    <tr class="head firstRow">
                                        <td width="150">列名称</td>
                                        <td>列描述</td>
                                    </tr>
                                    <tr>
                                        <td>username</td>
                                        <td><p>用户邮箱，必须填写</p></td>
                                    </tr>
                                    <tr>
                                        <td>name</td>
                                        <td><p>用户名称或姓名</p></td>
                                    </tr>
                                    <!-- <tr>
                                        <td>status</td>
                                        <td><p>用户状态：1，自由；0，锁定。不填写则默认为1</p></td>
                                    </tr> -->
                                    <tr>
                                        <td>role</td>
                                        <td><p>用户角色：end_user，普通用户；agent，客服人员；admin，管理员。不填写则默认为end_user</p></td>
                                    </tr>
                                    <tr>
                                        <td>phone</td>
                                        <td><p>联系电话</p></td>
                                    </tr>
                                    <tr>
                                        <td>details</td>
                                        <td><p>用户说明</p></td>
                                    </tr>
                                    <tr>
                                        <td>signature</td>
                                        <td>
                                            <p>用户签名　只有客服职员有签名，如果给普通用户命名此字段，将不会显示</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>notes</td>
                                        <td><p>用户详细信息</p></td>
                                    </tr>
                                </tbody>
                            </table>
                            <p><br></p>
                            <p>
                                <span style="text-decoration:underline;">从第二行开始撰写用户数据</span>
                            </p>
                            <p>例如： &nbsp; qnsoft@cdesk.com,cdesk帮助台管理,admin,13188888888,介绍信息,cdesk帮助台管理员,备注信息</p>
                            <p>
                                <br>
                                <strong><span style="color:#333333;">导入注意事项</span></strong>
                                <br>
                            </p>
                            <p>1、字段名请填写英文字母，尚未识别中文字段。</p>
                            <p>2、必须包括username列，其余列可以不填写。</p>
                            <p>3、字段列的顺序可以变动，不符合规定的列将被过滤掉。</p>
                            <p>4、字段列若重复出现，以排在前面的列为准。</p>
                            <!-- <p>5、当添加客服人员和管理员时，贵公司的坐席数会相应减少。如有需要，请扩充贵公司的坐席数量。</p> -->
                            <p><br></p>
                           <!--   <p><br></p>
                           <p><strong>导入公司组织</strong></p>
                            <p><strong><span style="color:#333333;">导入文件撰写规范和标准</span></strong></p>
                            <p><span style="text-decoration:underline;">CSV文件的第一行填写列名</span></p>
                            <p>列名为以下项目：</p>
                            <table class="content_table" cellspacing="0" cellpadding="0">
                                <tbody>
                                    <tr class="head firstRow">
                                        <td width="150">列名称</td>
                                        <td>列描述</td>
                                    </tr>
                                    <tr>
                                        <td>name</td>
                                        <td><p>公司组织名称，必须填写</p></td>
                                    </tr>
                                    <tr>
                                        <td>description</td>
                                        <td>
                                            <p>公司<span style="font-size:12px;line-height:normal;">组织</span>描述信息</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>domain</td>
                                        <td>
                                            <p>公司组织的域名匹配信息，当加入的用户邮箱的域名符合该域名自动加入该公司组织，如果留空则表示不进行域名匹配</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>group_id</td>
                                        <td>受理客服组编号，如果留空则表示不分配该公司组织人员的工单给该客服组受理</td>
                                    </tr>
                                </tbody>
                            </table>
                            <p><br></p>
                            <p><span style="text-decoration:underline;">从第二行开始撰写公司组织数据</span></p>
                            <p>例如： &nbsp; 北京青牛科技有限公司的描述,CDESK.com,</p>
                            <p>
                                <br>
                                <strong><span style="color:#333333;">导入注意事项</span></strong>
                                <br>
                            </p>
                            <p><br></p>
                            <p>1 公司名称必须填写，不能为空。其余字段可以不填。
                                <br>2 域名若是填写，必须符合域名格式。
                                <br>3 受理客服组编号如果不填，表示未分配客服组。如果填写，则必须为受理客服表中的编号。
                                <br>4 文件中行与行之间有间隔，不会影响导入过程。</p>
                            <p><br></p> -->
                            <p><br></p>
                            <p><strong>您可以在下面下载导入模板样本</strong></p>
                        </div>
                        <div class="attachment">
                            <p>附件：用户导入模板样本.zip&nbsp;•&nbsp;606B&nbsp;•&nbsp;
                                <a href="<%=request.getContextPath()%>/userImport/downModelExcel">下载</a>
                            </p>
                            <!-- <p>附件：组织导入模板样本.zip&nbsp;•&nbsp;268B&nbsp;•&nbsp;
                                <a href="">下载</a>
                            </p> -->
                        </div>
                    </div>
                    <!-- <footer class="article-footer">
                        <p>
                            <span>1</span> 人觉得有帮助
                            <a href="" class="article-vote-up">
                                <i class="fa fa-thumbs-o-up"></i>有帮助
                            </a>
                        </p>
                    </footer> -->
                </div>
                <%-- <div class="article-comments">
                    <h2>回复<span>（2）</span></h2>
                    <ul class="article-comments-list">
                        <li>
                            <img src="<%=request.getContextPath()%>/H+3.2/img/profile_small.jpg" class="article-comments-item-pic">
                            <div class="article-comments-item-header">
                                <span class="author">名字</span>·
                                <time>发表于 2012年08月17日  早上 10:42</time>
                            </div>
                            <div>
                                <div class="article-comments-item-content">向帮助台批量导入帮助台人员和公司组织</div>
                            </div>
                            <div class="signature">详情有问题请向我们提交工单，或者查看帮助文档参与帮助讨论解决问题。</div>
                        </li>
                        <li></li>
                    </ul>
                    <<!-- div class="info-block">您需要
                        <a href="#">登录</a>后才可以回复
                    </div> -->
                </div> --%>
            </section>
            <section class="content-sidebar">
                <!-- <div class="widget-column">
                    <h3>最多浏览的文档</h3>
                    <ul>
                        <li>
                            <a>如何给普通用户设置的语言切换及注意事项</a>
                        </li>
                        <li>
                            <a>第一课 解决您的第一个工单</a>
                        </li>
                        <li>
                            <a>如何绑定公司/企业自己的域名来代替二级域名</a>
                        </li>
                        <li>
                            <a>用触发器任务来精简工作流程</a>
                        </li>
                        <li>
                            <a>什么是工单系统，工单系统的用途和介绍</a>
                        </li>
                        <li>
                            <a>配置和使用电话语音支持渠道</a>
                        </li>
                        <li>
                            <a>逸创云客服工单系统默认规则详解</a>
                        </li>
                        <li>
                            <a>ECSHOP 网店系统 整合文档</a>
                        </li>
                        <li>
                            <a>使用服务时间设置来满足您企业实际的服务规则</a>
                        </li>
                        <li>
                            <a>使用工单查看分类来管理工作流程</a>
                        </li>
                    </ul>
                </div> -->
            </section>
        </div>

    </section>
</main>
</body>
</html>