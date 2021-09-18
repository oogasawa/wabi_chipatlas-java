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

<script type="text/javascript">

$(function(){

  $('#std_div_title').click(function(){
    if (this.checked) {
      $('.std_div').attr('checked', 'checked');
    }
    else {
      $('.std_div').removeAttr('checked');
    }
  });

  $('#ht_div_title').click(function(){
    if (this.checked) {
      $('.ht_div').attr('checked', 'checked');
    }
    else {
      $('.ht_div').removeAttr('checked');
    }
  });


  $('#est_div_title').click(function(){
    if (this.checked) {
      $('.est_div').attr('checked', 'checked');
    }
    else {
      $('.est_div').removeAttr('checked');
    }
  });

  $('#other_div_title').click(function(){
    if (this.checked) {
      $('.other_div').attr('checked', 'checked');
    }
    else {
      $('.other_div').removeAttr('checked');
    }
  });

});
</script>
