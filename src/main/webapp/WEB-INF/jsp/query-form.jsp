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

<div class="h1_design">
  <h1>${program_title}</h1>
  <!-- <h1>${program}</h2> -->
</div>

<!-- ************************************************************************ -->

<h2>Query</h2>
File Upload: <INPUT TYPE="file" NAME="up_file" MAXLENGTH="0"><br />
&nbsp;or COPY & PASTE:<br />
<TEXTAREA NAME="qseq" COLS=80 ROWS=10></TEXTAREA>
<br /> 
<INPUT TYPE="submit" VALUE="Send to BLAST"> 
<INPUT TYPE="button" VALUE="Clear"> 
<br />
<br />

