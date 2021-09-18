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

<div id="primary" class="widget-area" role="complementary">
	<ul class="xoxo">
		<div id="sub">
			<ul class="link01">
				<li><p>
						<a href="megablast?lang=${language_code2}">megablast</a>
					</p></li>
				<li><p>
						<a href="blastn?lang=${language_code2}">blastn</a>
					</p></li>
				<li><p>
						<a href="tblastn?lang=${language_code2}">tblastn</a>
					</p></li>
				<li><p>
						<a href="tblastx?lang=${language_code2}">tblastx</a>
					</p></li>
				<li><p>
						<a href="blastp?lang=${language_code2}">blastp</a>
					</p></li>
				<li><p>
						<a href="blastx?lang=${language_code2}">blastx</a><br><br>
					</p></li>
				<li><p>
						<a href="http://www.ddbj.nig.ac.jp/search/help/blasthelp-${language_code}.html">
						<spring:message code="label.side-menu.help" /></a>
					</p></li>

			</ul>
		</div>
	</ul>
</div>