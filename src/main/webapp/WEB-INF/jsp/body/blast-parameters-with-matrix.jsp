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


<h2 id="opheader">Optional Parameters</h2>
<table id="optable" border="0">
  <tr>
	<td width="50">SCORES</td>
	<td><INPUT TYPE="number" NAME="score" VALUE="100" SIZE="5"></td>
  </tr>
  <tr>
	<td width="50">ALIGNMENTS</td>
	<td><INPUT TYPE="number" NAME="alignments" VALUE="100"
			   SIZE="5"></td>
  </tr>
  <tr>
	<td width="50">EXPECT</td>
	<td><INPUT TYPE="number" NAME="expect" VALUE=10 SIZE=5></td>
  </tr>
  <tr>
	<td width="50">FILTER</td>
	<td><INPUT TYPE="radio" NAME="filter" VALUE="T"> ON 
        <INPUT TYPE="radio" NAME="filter" VALUE="F" CHECKED> OFF
    </td>
  </tr>
  <tr>
	<td width="50">SCORING MATRIX</td>
	<td><SELECT NAME="matrix">
		<OPTION VALUE="PAM30">PAM30
		<OPTION VALUE="PAM70">PAM70
		<OPTION VALUE="PAM250">PAM250
		<OPTION VALUE="BLOSUM45">BLOSUM45
		<OPTION VALUE="BLOSUM50">BLOSUM50
		<OPTION VALUE="BLOSUM62" SELECTED>BLOSUM62
		<OPTION VALUE="BLOSUM80">BLOSUM80
		<OPTION VALUE="BLOSUM90">BLOSUM90
	</SELECT></td>
  </tr>
  <tr>
	<td>WORD SIZE</td>
	<td><INPUT TYPE="number" ID="wordsize" NAME="wordsize"
			   VALUE="3" SIZE=5></td>
  </tr>
  <tr><td width="50">OTHER OPTIONS</td><td><INPUT TYPE="TEXT" NAME="options" SIZE="40"></td></tr>
</table>
