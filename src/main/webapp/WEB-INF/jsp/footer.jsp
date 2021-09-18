<%--

    This file is part of WABI : DDBJ WebAPIs for Biology.

    WABI : DDBJ WebAPIs for Biology is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    WABI : DDBJ WebAPIs for Biology is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with WABI : DDBJ WebAPIs for Biology.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="footer" role="contentinfo">
	<div id="footer"
		style="border-top: solid 1px #d3d3d3; padding: 10px; font-size: 100%; width: 800px;">
		<a href="http://www.ddbj.nig.ac.jp/DDBJ_site-${language_code}.html"><spring:message code="label.footer.site-map" /></a><c:if test="${'j' == language_code}">│<a
			href="http://www.ddbj.nig.ac.jp/notice/sitepolicy-${language_code}.html"><spring:message code="label.footer.site-policy" /></a>│<a
			href="http://www.ddbj.nig.ac.jp/notice-${language_code}.html"><spring:message code="label.footer.notice" /></a>│<a
			href="http://www.ddbj.nig.ac.jp/notice/pinfo-${language_code}.html"><spring:message code="label.footer.pinfo" /></a></c:if>

		<div class="lastmod"
			style="float: right; color: #666666; margin-right: 10px; font-size: 100%;">
			Last modified : March 31, 2012.</div>
		<address
			style="font-style: normal; font-size: 100%; color: #666666; margin-top: 20px;">Copyright
			DNA Data Bank of Japan. All Rights Reserved.</address>
	</div>
</div>