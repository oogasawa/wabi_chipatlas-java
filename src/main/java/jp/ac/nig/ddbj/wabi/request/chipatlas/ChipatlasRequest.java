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
package jp.ac.nig.ddbj.wabi.request.chipatlas;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import jp.ac.nig.ddbj.wabi.request.WabiRequest;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;


/** POSTメソッドでChipatlasのWebAPIを呼び出す時に用いられる入力データを表す.
 * 
 * @author oogasawa
 *
 */
public class ChipatlasRequest extends WabiRequest {
	
	// bed fileの内容。ファイルに出力されるので内容のチェックは不要。
	@NotEmpty
	String bedAFile;
	
	// bed fileの内容。フィアルに出力されるので内容のチェックは不要。
	@NotEmpty
	String bedBFile;

	// qsubに渡すオプション。これだけChipatlasPostRequestValidatorクラスで内容チェックする？
	@Pattern(regexp = "[a-zA-Z0-9_ .,=\\-/]*")
	String qsubOptions = null;

	// "Genomic regions"と"Gene list"の二択？ 送られてくるのは名前？ID？
	@NotEmpty
	@Pattern(regexp = "[a-zA-Z0-9_ ]*")
	String typeA;

	// "RefSeq coding genes except 'My data'"と"Another gene list"の二択？ 送られてくるのは名前？ID？
	@NotEmpty
	@Pattern(regexp = "[a-zA-Z0-9_ ]*")
	String typeB;
	
	@NotNull
	@Range(min = 1, max = 100)
	int permTime;
	
	@NotNull
	int distanceUp;
	
	@NotNull
	int distanceDown;
	
	@NotEmpty
	@Pattern(regexp = "[a-zA-Z0-9_ .\\-]*")
	String descriptionA;
	
	@NotEmpty
	@Pattern(regexp = "[a-zA-Z0-9_ .\\-]*")
	String descriptionB;
	
	//Job title。文字種制限・文字数制限を付けるか？
	@NotEmpty
	@Pattern(regexp = "[a-zA-Z0-9_ .\\-]*")
	String title;
	
	// 選択したAntigen Class。送られてくるのは名前？ID？
	@NotEmpty
	@Pattern(regexp = "[a-zA-Z0-9_ .\\-]*")
	String antigenClass;
	
	// 選択したCell Type Class。送られてくるのは名前？ID？
	@NotEmpty
	@Pattern(regexp = "[a-zA-Z0-9_ .\\-]*")
	String cellClass;
	
	// 選択したgenome
	@NotEmpty
	@Pattern(regexp = "[a-zA-Z0-9_ ]*")
	String genome;
	
	// Quality Value Thresholdの値。50, 100, 200 or 500？
	@NotNull
	@Range(min = 0)
	int threshold;

	public String getBedAFile() {
		return bedAFile;
	}
	public void setBedAFile(String bedAFile) {
		this.bedAFile = bedAFile;
	}

	public String getBedBFile() {
		return bedBFile;
	}
	public void setBedBFile(String bedBFile) {
		this.bedBFile = bedBFile;
	}

	public String getQsubOptions() {
		return qsubOptions;
	}
	public void setQsubOptions(String qsubOptions) {
		this.qsubOptions = qsubOptions;
	}
	
	public String getTypeA() {
		return typeA;
	}
	public void setTypeA(String typeA) {
		this.typeA = typeA;
	}
	
	public String getTypeB() {
		return typeB;
	}
	public void setTypeB(String typeB) {
		this.typeB = typeB;
	}
	
	public int getPermTime() {
		return permTime;
	}
	public void setPermTime(int permTime) {
		this.permTime = permTime;
	}

	public int getDistanceUp() {
		return distanceUp;
	}
	public void setDistanceUp(int distanceUp) {
		this.distanceUp = distanceUp;
	}

	public int getDistanceDown() {
		return distanceDown;
	}
	public void setDistanceDown(int distanceDown) {
		this.distanceDown = distanceDown;
	}
	
	public String getDescriptionA() {
		return descriptionA;
	}
	public void setDescriptionA(String descriptionA) {
		this.descriptionA = descriptionA;
	}
	
	public String getDescriptionB() {
		return descriptionB;
	}
	public void setDescriptionB(String descriptionB) {
		this.descriptionB = descriptionB;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getAntigenClass() {
		return antigenClass;
	}
	public void setAntigenClass(String antigenClass) {
		this.antigenClass = antigenClass;
	}
	
	public String getCellClass() {
		return cellClass;
	}
	public void setCellClass(String cellClass) {
		this.cellClass = cellClass;
	}
	
	public String getGenome() {
		return genome;
	}
	public void setGenome(String genome) {
		this.genome = genome;
	}
	
	public int getThreshold() {
		return threshold;
	}
	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}
	
}
