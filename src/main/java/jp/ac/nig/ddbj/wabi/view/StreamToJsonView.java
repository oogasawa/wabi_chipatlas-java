/**
 * This file is part of WABI : DDBJ WebAPIs for Biology.
 *
 * WABI : DDBJ WebAPIs for Biology is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WABI : DDBJ WebAPIs for Biology is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with WABI : DDBJ WebAPIs for Biology.  If not, see <http://www.gnu.org/licenses/>.
 */
package jp.ac.nig.ddbj.wabi.view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.LinkedHashMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ogalab.util.os.FileIO;

import org.springframework.web.servlet.view.AbstractView;

import net.arnx.jsonic.JSON;

import jp.ac.nig.ddbj.wabi.util.StringUtil;

public class StreamToJsonView extends AbstractView {

	@Override
	protected void renderMergedOutputModel( Map<String, Object> model, HttpServletRequest req, HttpServletResponse res ) throws Exception {


		// Check if content type is specified.
		String contentType = "application/json; charset=utf-8";
		String characterEncoding = "UTF-8";

		// Set content type and character encoding as given/determined.
		res.setContentType( contentType );
		if ( characterEncoding != null )
			res.setCharacterEncoding( characterEncoding );

		
		// Make string to view.
		ServletOutputStream out = res.getOutputStream();
		PrintWriter    pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
		try {
			String file = (String)model.get("filename");
			if (null==file) {
				throw new IOException();
			}
			BufferedReader br = FileIO.getBufferedReader(file);

			StringBuilder buffer = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				buffer.append(line);
				buffer.append('\n');
			}
			br.close();
			String content = buffer.toString();
			String encoded;
			try {
				LinkedHashMap<String, String> mapJson = JSON.decode(content, LinkedHashMap.class);
				Map<String, String> mapEscaped = new LinkedHashMap<String, String>();
				for (String key : mapJson.keySet()) {
//					mapEscaped.put(key, StringUtil.encodeURI(mapJson.get(key)));
					mapEscaped.put(key, mapJson.get(key));
				}
				encoded = JSON.encode(mapEscaped, true);
			} catch (Exception ignore) {
				encoded = StringUtil.encodeURI(content);
			}
			pw.println(encoded);
		} catch (IOException e) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			pw.println("Not found.");
		}
		pw.close();

	}
	
}
