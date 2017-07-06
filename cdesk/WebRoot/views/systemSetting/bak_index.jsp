<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>控制面板首页</title>
    <%@include file="/views/include/pageHeader.jsp"%>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/innerFrame.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/console.css">
</head>
<body>
<div id="left-part">
    <header class="part-header">
        <ul class="nav nav-pills">
            <li><button class="btn btn-default btn-sm active">首页</button></li>
            <li><button class="btn btn-default btn-sm">新手向导</button></li>
        </ul>
    </header>
    <div class="left-content">
        <div class="left-content-top left-content-panel">
            <h2 class="left-content-panel-header">官方信息</h2>
            <ul class="left-content-top notice">
                <li>平台通知1</li>
                <li>公司通告1</li>
            </ul>
        </div>
        <div class="left-content-bottom left-content-panel">
            <h2 class="left-content-panel-header">工单状态</h2>
            <div class="timeline">
                <div class="timeline-item">
                    <div class="row">
                        <div class="col-xs-3 date">
                            <i class="fa fa-briefcase"></i> 6:00
                            <br>
                            <small class="text-navy">2 小时前</small>
                        </div>
                        <div class="col-xs-7 content no-top-border">
                            <p class="m-b-xs"><strong>检查机房1</strong></p>
                            <p>还未检查出错误</p>
                            <p><span data-diameter="40" class="updating-chart" style="display: none;">3,9,6,5,9,4,7,3,2,9,8,7,4,5,1,2,9,5,4,7,2,7,7,3,5,2,3,3,2,1,6,9,8,8,3,7,4</span>
                                <svg class="peity" height="16" width="64">
                                    <polygon fill="#1ab394" points="0 15 0 10.5 1.7777777777777777 0.5 3.5555555555555554 5.5 5.333333333333333 7.166666666666666 7.111111111111111 0.5 8.88888888888889 8.833333333333332 10.666666666666666 3.833333333333332 12.444444444444443 10.5 14.222222222222221 12.166666666666666 16 0.5 17.77777777777778 2.166666666666666 19.555555555555554 3.833333333333332 21.333333333333332 8.833333333333332 23.11111111111111 7.166666666666666 24.888888888888886 13.833333333333334 26.666666666666664 12.166666666666666 28.444444444444443 0.5 30.22222222222222 7.166666666666666 32 8.833333333333332 33.77777777777778 3.833333333333332 35.55555555555556 12.166666666666666 37.33333333333333 3.833333333333332 39.11111111111111 3.833333333333332 40.888888888888886 10.5 42.666666666666664 7.166666666666666 44.44444444444444 12.166666666666666 46.22222222222222 10.5 48 10.5 49.77777777777777 12.166666666666666 51.55555555555555 13.833333333333334 53.33333333333333 5.5 55.11111111111111 0.5 56.888888888888886 2.166666666666666 58.666666666666664 2.166666666666666 60.44444444444444 10.5 62.22222222222222 3.833333333333332 64 8.833333333333332 64 15"></polygon>
                                    <polyline fill="transparent" points="0 10.5 1.7777777777777777 0.5 3.5555555555555554 5.5 5.333333333333333 7.166666666666666 7.111111111111111 0.5 8.88888888888889 8.833333333333332 10.666666666666666 3.833333333333332 12.444444444444443 10.5 14.222222222222221 12.166666666666666 16 0.5 17.77777777777778 2.166666666666666 19.555555555555554 3.833333333333332 21.333333333333332 8.833333333333332 23.11111111111111 7.166666666666666 24.888888888888886 13.833333333333334 26.666666666666664 12.166666666666666 28.444444444444443 0.5 30.22222222222222 7.166666666666666 32 8.833333333333332 33.77777777777778 3.833333333333332 35.55555555555556 12.166666666666666 37.33333333333333 3.833333333333332 39.11111111111111 3.833333333333332 40.888888888888886 10.5 42.666666666666664 7.166666666666666 44.44444444444444 12.166666666666666 46.22222222222222 10.5 48 10.5 49.77777777777777 12.166666666666666 51.55555555555555 13.833333333333334 53.33333333333333 5.5 55.11111111111111 0.5 56.888888888888886 2.166666666666666 58.666666666666664 2.166666666666666 60.44444444444444 10.5 62.22222222222222 3.833333333333332 64 8.833333333333332" stroke="#169c81" stroke-width="1" stroke-linecap="square"></polyline>
                                </svg>
                            </p>
                        </div>
                    </div>
                </div>
                <div class="timeline-item">
                    <div class="row">
                        <div class="col-xs-3 date">
                            <i class="fa fa-file-text"></i>
                            时间点
                            <br>
                            <small class="text-navy">时间细节</small>
                        </div>
                        <div class="col-xs-7 content">
                            <p class="m-b-xs"><strong>事情概要</strong></p>
                            <p>事情详情</p>
                        </div>
                    </div>
                </div>
                <div class="timeline-item">
                    <div class="row">
                        <div class="col-xs-3 date">
                            <i class="fa fa-coffee"></i> 8:00
                            <br>
                        </div>
                        <div class="col-xs-7 content">
                            <p class="m-b-xs"><strong>检查机房3</strong></p>
                            <p>
                                启动服务
                            </p>
                        </div>
                    </div>
                </div>
                <div class="timeline-item">
                    <div class="row">
                        <div class="col-xs-3 date">
                            <i class="fa fa-phone"></i> 11:00
                            <br>
                            <small class="text-navy">21小时前</small>
                        </div>
                        <div class="col-xs-7 content">
                            <p class="m-b-xs"><strong>检查机房4</strong>
                            </p>
                            <p>
                                启动服务
                            </p>
                        </div>
                    </div>
                </div>
                <div class="timeline-item">
                    <div class="row">
                        <div class="col-xs-3 date">
                            <i class="fa fa-user-md"></i> 09:00
                            <br>
                            <small>21小时前</small>
                        </div>
                        <div class="col-xs-7 content">
                            <p class="m-b-xs"><strong>检查机房5</strong>
                            </p>
                            <p>
                                启动服务
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="right-part">
    <header class="part-header">
        <ul class="nav nav-pills">
            <li><button class="btn btn-default btn-sm active">工单处理概况</button></li>
            <li><button class="btn btn-default btn-sm">最近动向统计</button></li>
        </ul>
    </header>
    <div class="right-content">
        <div class="content-right-panel container">
            <div class="row">
                <nav class="panel-number-btn">
                    <ul>
                        <li id="ember1478" class="ember-view">
                            <a><span class="red">5</span>
                                <p>我处理中的工单</p>
                            </a></li>
                        <li id="ember1479" class="ember-view active">
                            <a><span class="blue">111</span><p>我客服组处理中的工单</p></a></li>
                        <li id="ember1480" class="ember-view">
                            <a><span class="green">1</span><p>我客服组里未分配的工单</p></a>
                        </li>
                        <li id="ember1481" class="ember-view">
                            <a><span class="orange">0</span><p>有满意度评价的工单</p></a>
                        </li>
                        <li id="ember1482" class="ember-view"><a><span class="purple">0</span><p>知识库文档</p></a></li>
                    </ul>
                </nav>
            </div>
            <div class="row table-content">
                <div class="col-12 grid">
                    <table class="table ticket footable table-stripped toggle-arrow-tiny" cellspacing="0" cellpadding="0" id="orderGrid" data-page-size="10">
                        <thead>
                        <tr class="order">
                            <th><input id="ember1571" class="ember-view ember-checkbox all-checkbox" type="checkbox"> </th>
                            <th>星标</th><th data-ember-action="1572">编号 <span id="ember1580" class="ember-view" style="display:none;"> <i class="icon-expand-less"></i> </span></th>
                            <th data-ember-action="1581">标题 <span id="ember1583" class="ember-view" style="display:none;"> <i class="icon-expand-less"></i> </span></th>
                            <th data-ember-action="1584">工单发起人 <span id="ember1586" class="ember-view" style="display:none;"> <i class="icon-expand-less"></i> </span></th>
                            <th data-ember-action="1587">创建日期 <span id="ember1589" class="ember-view" style="display:none;"> <i class="icon-expand-less"></i> </span></th>
                            <th data-ember-action="1590">受理客服组 <span id="ember1592" class="ember-view" style="display:none;"> <i class="icon-expand-less"></i> </span></th>
                            <th data-ember-action="1593">工单受理客服 <span id="ember1595" class="ember-view" style="display:none;"> <i class="icon-expand-less"></i> </span></th>
                        </tr></thead>
                        <tbody>
                        <tr id="ember1596" class="ember-view">
                            <td> <input id="ember1614" class="ember-view ember-checkbox table-checkbox" type="checkbox"> </td>
                            <td class="mark" data-ember-action="1615"><a class="mark-star"><i class="icon-star"></i></a></td>
                            <td id="ember1618" class="ember-view id">    2844  </td>
                            <td id="ember1620" class="ember-view title">  <a id="ember1627" class="ember-view tooltipstered">呼出至 15330067785 的语音电</a>    </td>
                            <td id="ember1629" class="ember-view requester_id">    15330067785  </td>
                            <td id="ember1631" class="ember-view created">    11月12日 10:31  </td>
                            <td id="ember1633" class="ember-view group_id">    财务组  </td>
                            <td id="ember1635" class="ember-view assignee_id">    demo  </td>
                        </tr>
                        <tr id="ember1637" class="ember-view"> <td> <input id="ember1649" class="ember-view ember-checkbox table-checkbox" type="checkbox"> </td> <td class="mark" data-ember-action="1650"><a class="mark-star"><i class="icon-star"></i></a></td>  <td id="ember1651" class="ember-view id">    2839  </td>  <td id="ember1653" class="ember-view title">  <a id="ember1654" class="ember-view tooltipstered">来自 18008071065 的语音电话</a>    </td>  <td id="ember1656" class="ember-view requester_id">    18008071065  </td>  <td id="ember1658" class="ember-view created">    11月11日 21:34  </td>  <td id="ember1660" class="ember-view group_id">    行政组  </td>  <td id="ember1662" class="ember-view assignee_id">    刘铭  </td>  </tr>
                        <tr id="ember1664" class="ember-view"> <td> <input id="ember1676" class="ember-view ember-checkbox table-checkbox" type="checkbox"> </td> <td class="mark" data-ember-action="1677"><a class="mark-star"><i class="icon-star"></i></a></td>  <td id="ember1678" class="ember-view id">    2838  </td>  <td id="ember1680" class="ember-view title">  <a id="ember1681" class="ember-view tooltipstered">呼出至 13458640486 的语音电</a>    </td>  <td id="ember1683" class="ember-view requester_id">    13458640486  </td>  <td id="ember1685" class="ember-view created">    11月11日 21:18  </td>  <td id="ember1687" class="ember-view group_id">    行政组  </td>  <td id="ember1689" class="ember-view assignee_id">    刘铭  </td>  </tr>   <tr id="ember1691" class="ember-view"> <td> <input id="ember1703" class="ember-view ember-checkbox table-checkbox" type="checkbox"> </td> <td class="mark" data-ember-action="1704"><a class="mark-star"><i class="icon-star"></i></a></td>  <td id="ember1705" class="ember-view id">    2837  </td>  <td id="ember1707" class="ember-view title">  <a id="ember1708" class="ember-view tooltipstered">呼出至 13458640486 的语音电</a>    </td>  <td id="ember1710" class="ember-view requester_id">    13458640486  </td>  <td id="ember1712" class="ember-view created">    11月11日 21:16  </td>  <td id="ember1714" class="ember-view group_id">    行政组  </td>  <td id="ember1716" class="ember-view assignee_id">    刘铭  </td>  </tr>   <tr id="ember1718" class="ember-view"> <td> <input id="ember1730" class="ember-view ember-checkbox table-checkbox" type="checkbox"> </td> <td class="mark" data-ember-action="1731"><a class="mark-star"><i class="icon-star"></i></a></td>  <td id="ember1732" class="ember-view id">    2832  </td>  <td id="ember1734" class="ember-view title">  <a id="ember1735" class="ember-view tooltipstered">呼出至 18810407491 的语音电</a>    </td>  <td id="ember1737" class="ember-view requester_id">    18810407491  </td>  <td id="ember1739" class="ember-view created">    11月11日 11:52  </td>  <td id="ember1741" class="ember-view group_id">    财务组  </td>  <td id="ember1743" class="ember-view assignee_id">    demo  </td>  </tr>   <tr id="ember1745" class="ember-view"> <td> <input id="ember1757" class="ember-view ember-checkbox table-checkbox" type="checkbox"> </td> <td class="mark" data-ember-action="1758"><a class="mark-star"><i class="icon-star"></i></a></td>  <td id="ember1759" class="ember-view id">    2831  </td>  <td id="ember1761" class="ember-view title">  <a id="ember1762" class="ember-view tooltipstered">呼出至 18810407491 的语音电</a>    </td>  <td id="ember1764" class="ember-view requester_id">    18810407491  </td>  <td id="ember1766" class="ember-view created">    11月11日 11:50  </td>  <td id="ember1768" class="ember-view group_id">    财务组  </td>  <td id="ember1770" class="ember-view assignee_id">    demo  </td>  </tr>   <tr id="ember1772" class="ember-view"> <td> <input id="ember1784" class="ember-view ember-checkbox table-checkbox" type="checkbox"> </td> <td class="mark" data-ember-action="1785"><a class="mark-star"><i class="icon-star"></i></a></td>  <td id="ember1786" class="ember-view id">    2830  </td>  <td id="ember1788" class="ember-view title">  <a id="ember1789" class="ember-view tooltipstered">呼出至 Tommy 的语音电话</a>    </td>  <td id="ember1791" class="ember-view requester_id">    Tommy  </td>  <td id="ember1793" class="ember-view created">    11月11日 11:48  </td>  <td id="ember1795" class="ember-view group_id">    财务组  </td>  <td id="ember1797" class="ember-view assignee_id">    demo  </td>  </tr>   <tr id="ember1799" class="ember-view"> <td> <input id="ember1811" class="ember-view ember-checkbox table-checkbox" type="checkbox"> </td> <td class="mark" data-ember-action="1812"><a class="mark-star"><i class="icon-star"></i></a></td>  <td id="ember1813" class="ember-view id">    2828  </td>  <td id="ember1815" class="ember-view title">  <a id="ember1816" class="ember-view tooltipstered">呼出至 Tommy 的语音电话</a>    </td>  <td id="ember1818" class="ember-view requester_id">    Tommy  </td>  <td id="ember1820" class="ember-view created">    11月11日 11:27  </td>  <td id="ember1822" class="ember-view group_id">    财务组  </td>  <td id="ember1824" class="ember-view assignee_id">    demo  </td>  </tr>   <tr id="ember1826" class="ember-view"> <td> <input id="ember1838" class="ember-view ember-checkbox table-checkbox" type="checkbox"> </td> <td class="mark" data-ember-action="1839"><a class="mark-star"><i class="icon-star"></i></a></td>  <td id="ember1840" class="ember-view id">    2825  </td>  <td id="ember1842" class="ember-view title">  <a id="ember1843" class="ember-view tooltipstered">转发：yichuangceshi</a>    </td>  <td id="ember1845" class="ember-view requester_id">    18684030998  </td>  <td id="ember1847" class="ember-view created">    11月10日 19:48  </td>  <td id="ember1849" class="ember-view group_id">    财务组  </td>  <td id="ember1851" class="ember-view assignee_id">    -  </td>  </tr>   <tr id="ember1853" class="ember-view"> <td> <input id="ember1865" class="ember-view ember-checkbox table-checkbox" type="checkbox"> </td> <td class="mark" data-ember-action="1866"><a class="mark-star"><i class="icon-star"></i></a></td>  <td id="ember1867" class="ember-view id">    2824  </td>  <td id="ember1869" class="ember-view title">  <a id="ember1870" class="ember-view tooltipstered">来自 李成 的聊天对话</a>    </td>  <td id="ember1872" class="ember-view requester_id">    李成chris@逸创云客服  </td>  <td id="ember1874" class="ember-view created">    11月10日 18:56  </td>  <td id="ember1876" class="ember-view group_id">    行政组  </td>  <td id="ember1878" class="ember-view assignee_id">    缇娜  </td>  </tr>   <tr id="ember1880" class="ember-view"> <td> <input id="ember1892" class="ember-view ember-checkbox table-checkbox" type="checkbox"> </td> <td class="mark" data-ember-action="1893"><a class="mark-star"><i class="icon-star"></i></a></td>  <td id="ember1894" class="ember-view id">    2823  </td>  <td id="ember1896" class="ember-view title">  <a id="ember1897" class="ember-view tooltipstered">呼出至 14782435835 的语音电</a>    </td>  <td id="ember1899" class="ember-view requester_id">    测试啊  </td>  <td id="ember1901" class="ember-view created">    11月10日 17:35  </td>  <td id="ember1903" class="ember-view group_id">    财务组  </td>  <td id="ember1905" class="ember-view assignee_id">    -  </td>  </tr>   <tr id="ember1907" class="ember-view"> <td> <input id="ember1919" class="ember-view ember-checkbox table-checkbox" type="checkbox"> </td> <td class="mark" data-ember-action="1920"><a class="mark-star"><i class="icon-star"></i></a></td>  <td id="ember1921" class="ember-view id">    2817  </td>  <td id="ember1923" class="ember-view title">  <a id="ember1924" class="ember-view tooltipstered">测试 别别别</a>    </td>  <td id="ember1926" class="ember-view requester_id">    测试啊  </td>  <td id="ember1928" class="ember-view created">    11月10日 11:15  </td>  <td id="ember1930" class="ember-view group_id">    财务组  </td>  <td id="ember1932" class="ember-view assignee_id">    -  </td>  </tr>   <tr id="ember1934" class="ember-view"> <td> <input id="ember1946" class="ember-view ember-checkbox table-checkbox" type="checkbox"> </td> <td class="mark" data-ember-action="1947"><a class="mark-star"><i class="icon-star"></i></a></td>  <td id="ember1948" class="ember-view id">    2815  </td>  <td id="ember1950" class="ember-view title">  <a id="ember1951" class="ember-view tooltipstered">测试啊</a>    </td>  <td id="ember1953" class="ember-view requester_id">    测试啊  </td>  <td id="ember1955" class="ember-view created">    11月10日 10:49  </td>  <td id="ember1957" class="ember-view group_id">    财务组  </td>  <td id="ember1959" class="ember-view assignee_id">    -  </td>  </tr>   <tr id="ember1961" class="ember-view"> <td> <input id="ember1973" class="ember-view ember-checkbox table-checkbox" type="checkbox"> </td> <td class="mark" data-ember-action="1974"><a class="mark-star"><i class="icon-star"></i></a></td>  <td id="ember1975" class="ember-view id">    2813  </td>  <td id="ember1977" class="ember-view title">  <a id="ember1978" class="ember-view tooltipstered">来自 liuming 的聊天对话</a>    </td>  <td id="ember1980" class="ember-view requester_id">    刘铭  </td>  <td id="ember1982" class="ember-view created">    11月09日 19:21  </td>  <td id="ember1984" class="ember-view group_id">    行政组  </td>  <td id="ember1986" class="ember-view assignee_id">    刘铭  </td>  </tr>   <tr id="ember1988" class="ember-view"> <td> <input id="ember2000" class="ember-view ember-checkbox table-checkbox" type="checkbox"> </td> <td class="mark" data-ember-action="2001"><a class="mark-star"><i class="icon-star"></i></a></td>  <td id="ember2002" class="ember-view id">    2811  </td>  <td id="ember2004" class="ember-view title">  <a id="ember2005" class="ember-view tooltipstered">来自 谢渝 的聊天对话</a>    </td>  <td id="ember2007" class="ember-view requester_id">    谢渝  </td>  <td id="ember2009" class="ember-view created">    11月09日 19:10  </td>  <td id="ember2011" class="ember-view group_id">    行政组  </td>  <td id="ember2013" class="ember-view assignee_id">    刘铭  </td>  </tr>
                        <tr id="ember2015" class="ember-view"> <td> <input id="ember2027" class="ember-view ember-checkbox table-checkbox" type="checkbox"> </td> <td class="mark" data-ember-action="2028"><a class="mark-star"><i class="icon-star"></i></a></td>  <td id="ember2029" class="ember-view id">    2808  </td>  <td id="ember2031" class="ember-view title">  <a id="ember2032" class="ember-view tooltipstered">呼出至 13918485085 的语音电</a>    </td>  <td id="ember2034" class="ember-view requester_id">    13918485085  </td>  <td id="ember2036" class="ember-view created">    11月09日 17:22  </td>  <td id="ember2038" class="ember-view group_id">    财务组  </td>  <td id="ember2040" class="ember-view assignee_id">    -  </td>  </tr>
                        <tr id="ember2042" class="ember-view"> <td> <input id="ember2054" class="ember-view ember-checkbox table-checkbox" type="checkbox"> </td> <td class="mark" data-ember-action="2055"><a class="mark-star"><i class="icon-star"></i></a></td>  <td id="ember2056" class="ember-view id">    2807  </td>  <td id="ember2058" class="ember-view title">  <a id="ember2059" class="ember-view tooltipstered">来自 18612419450 的语音电话</a>    </td>  <td id="ember2061" class="ember-view requester_id">    18612419450  </td>  <td id="ember2063" class="ember-view created">    11月09日 17:20  </td>  <td id="ember2065" class="ember-view group_id">    财务组  </td>  <td id="ember2067" class="ember-view assignee_id">    -  </td>  </tr>
                        <tr id="ember2069" class="ember-view"> <td> <input id="ember2081" class="ember-view ember-checkbox table-checkbox" type="checkbox"> </td> <td class="mark" data-ember-action="2082"><a class="mark-star"><i class="icon-star"></i></a></td>  <td id="ember2083" class="ember-view id">    2806  </td>  <td id="ember2085" class="ember-view title">  <a id="ember2086" class="ember-view tooltipstered">呼出至 18612419450 的语音电</a>    </td>  <td id="ember2088" class="ember-view requester_id">    18612419450  </td>  <td id="ember2090" class="ember-view created">    11月09日 17:17  </td>  <td id="ember2092" class="ember-view group_id">    财务组  </td>  <td id="ember2094" class="ember-view assignee_id">    -  </td>  </tr>
                        <tr id="ember2096" class="ember-view"> <td> <input id="ember2108" class="ember-view ember-checkbox table-checkbox" type="checkbox"> </td> <td class="mark" data-ember-action="2109"><a class="mark-star"><i class="icon-star"></i></a></td>  <td id="ember2110" class="ember-view id">    2805  </td>  <td id="ember2112" class="ember-view title">  <a id="ember2113" class="ember-view tooltipstered">呼出至 18612419450 的语音电</a>    </td>  <td id="ember2115" class="ember-view requester_id">    18612419450  </td>  <td id="ember2117" class="ember-view created">    11月09日 17:14  </td>  <td id="ember2119" class="ember-view group_id">    财务组  </td>  <td id="ember2121" class="ember-view assignee_id">    -  </td>  </tr>
                        <tr id="ember2123" class="ember-view"> <td> <input id="ember2135" class="ember-view ember-checkbox table-checkbox" type="checkbox"> </td> <td class="mark" data-ember-action="2136"><a class="mark-star"><i class="icon-star"></i></a></td>  <td id="ember2137" class="ember-view id">    2804  </td>  <td id="ember2139" class="ember-view title">  <a id="ember2140" class="ember-view tooltipstered">呼出至 13918485085 的语音电</a>    </td>  <td id="ember2142" class="ember-view requester_id">    13918485085  </td>  <td id="ember2144" class="ember-view created">    11月09日 16:55  </td>  <td id="ember2146" class="ember-view group_id">    财务组  </td>  <td id="ember2148" class="ember-view assignee_id">    -  </td>  </tr>
                        </tbody>
                        <tfoot>
                        <tr>
                            <td colspan="8">
                                <ul class="pagination pull-right"></ul>
                            </td>
                        </tr>
                        </tfoot>
                    </table>

                </div>
            </div>

        </div>
    </div>
</div>
<script src="<%=request.getContextPath()%>/H+3.2/js/plugins/footable/footable.all.min.js"></script>
<script>
    $(document).ready(function() {
        $('#orderGrid').footable();
    });
</script>
</body>
</html>