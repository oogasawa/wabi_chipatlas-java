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
package jp.ac.nig.ddbj.wabi.job.chipatlas;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.ac.nig.ddbj.wabi.controller.chipatlas.ChipatlasController;
import jp.ac.nig.ddbj.wabi.job.WabiJobInfo;
import jp.ac.nig.ddbj.wabi.util.Conf;
import net.ogalab.util.container.ArrayUtil;
import net.ogalab.util.fundamental.Type;
import net.ogalab.util.linux.Bash;
import net.ogalab.util.linux.BashResult;
import net.ogalab.util.os.FileIO;
import net.ogalab.util.rand.RNG;
import cern.jet.random.Uniform;

public class ChipatlasJobInfo extends WabiJobInfo {
	
	Pattern pRequestId = Pattern.compile("(.+?)_([0-9]{4})-([0-9]{2})([0-9]{2})-([0-9]{2})([0-9]{2})-([0-9]{2})-([0-9]+)-([0-9]+)");
	
	Pattern p1 = Pattern.compile("^Following jobs do not exists", Pattern.MULTILINE);
	Pattern p2 = Pattern.compile("^usage\\s+", Pattern.MULTILINE);
	Pattern p3 = Pattern.compile("^job_number:\\s+", Pattern.MULTILINE);
	Pattern p4 = Pattern.compile("^scheduling info:\\s+There are no messages available", Pattern.MULTILINE);

	/** このディレクトリ以下にWebAPIによるプログラム実行結果などジョブの情報が置かれる */
//	public static String workingDirRoot = Conf.workingDirBase;
	
	/** Random Number Generator Object (singleton) */
	RNG engine = null;

	/** ロックオブジェクト */
//	private final static Object lock = new Object();
	

	public ChipatlasJobInfo(RNG e) {
		super(e);
	}
	
	/** requestIdの文字列をパースすることによりjobInfoオブジェクトを作る。
	 * 
	 * @param requestId
	 * @throws IOException jobIDファイルがない。requestIdの文字列がおかしいかもしれません。
	 */
	public ChipatlasJobInfo(String requestId) throws IOException {
		super(requestId);
	}
	
	/** UGEのqstat, qacctを呼び出し、jobIdで表されるジョブの現在の状況を返す.
	 * 
	 * ジョブの現在の状況は、以下のいずれかになる。 
	 * <ul>
	 * <li>"waiting"</li>
	 * <li>"running"</li>
	 * <li>"finished"</li>
	 * <li>"not-found"</li>
	 * </ul>
	 * @throws IOException 
	 * 
	 */

	public boolean existsFinishedFile() {
		String path = getWorkingDir() + ChipatlasController.FINISHED_FILE;
		return new File(path).exists();
	}

	public boolean existsOutFile() {
		String path = getWorkingDir() + ChipatlasController.WABI_OUT_FILE;
		return new File(path).exists();
	}
	
	public boolean existsTsvOutFile() {
		String path = getWorkingDir() + ChipatlasController.WABI_TSV_OUT_FILE;
		return new File(path).exists();
	}

	public boolean existsHtmlOutFile() {
		String path = getWorkingDir() + ChipatlasController.WABI_HTML_OUT_FILE;
		return new File(path).exists();
	}

	/** jobIDがかかれたファイルの中身を読んでjobIDを取得し、それを文字列として返す。
	 * /home/geadmin/UGEB/ugeb/common/settings.sh
	 * リクエストは既に実行されていてjobIDは既に発行されていると前提している。
	 * 
	 * @return jobID
	 * @throws IOException 
	 */
	public String readJobId() throws IOException {
		String path = getWorkingDir() + ChipatlasController.UGE_JOB_ID_FILE;
		//String id   = FileIO.readFile(path);
		String id = "";
		if (new File(path).exists())
			id = FileIO.readFile(path);
		
		return id.trim();
	}

	/** requestIdが存在しなければ作成し、オブジェクトに登録する。既にオブジェクトに登録されているのであれば何もしない。
	 * 
	 * @return requestId
	 * @throws IOException 
	 */
	public String generateRequestId() throws IOException {
		String id = null;
		if (requestId == null)
			id = reGenerateRequestId();
		else
			id = requestId;
		
		return id;
	}
	
	/** requestIdが既に発行されているかどうかに関わらずrequestIdを再発行し、オブジェクトに登録する。
	 *  対応するディレクトリも作成する。（同一IDが既にとられていないかを判定するため）.
	 * 
	 * @return requestId
	 * @throws IOException 
	 */
	public String reGenerateRequestId() throws IOException {
		Calendar d = Calendar.getInstance();
		year      = String.format("%1$tY", d);
		month     = String.format("%1$tm", d);
		day       = String.format("%1$td", d);
		hour      = String.format("%1$tH", d);
		min       = String.format("%1$tM", d);
		sec       = String.format("%1$tS", d);
		millisec  = Type.toString(d.get(Calendar.MILLISECOND));
		

		// TODO 乱数発生要注意
		Uniform unif = new Uniform(engine.getEngine());
		randomSuffix  = Type.toString(unif.nextIntFromTo(0, 1000000), 6);
		
		requestId = ChipatlasController.outfilePrefix + ArrayUtil.join("-", new String[]{year, month+day, hour+min, sec, millisec, randomSuffix}).trim();
		String dir = getWorkingDir(); // ディレクトリの名前だけが作られる。
		//ID重複防止のためロックする
		synchronized (lock) {
			// IDの重複がある場合はID発行やり直し
			if (new File(dir).exists()) // そのディレクトリが既にある
				requestId = reGenerateRequestId();
		
			makeWorkingDir();
		}
		return requestId;
	}
	
	public boolean existsUserRequestFile() {
		String path = getWorkingDir() + ChipatlasController.USER_REQUEST_FILE;
		return new File(path).exists();
	}
	
	/** Jobが動作するWorking Directoryのフルパス名をStringとして返す.
	 * 
	 * makeWorkingDir()メソッドを呼ぶまではディレクトリの実体は作成されていないかもしれない。
	 * 
	 * @return Working Directoryのフルパス名
	 */
	public String getWorkingDir() {
		return workingDirRoot + ArrayUtil.join("/", new String[]{year, month+day, hour+min, sec, millisec, randomSuffix}).trim() + "/";
	}
	
	public LinkedHashMap<String, String> getInfo(boolean withWorkingDirRoot) {
		LinkedHashMap<String, String> info = new LinkedHashMap<String, String>();
		info.put("requestId", requestId);
		info.put("jobId", jobId);
		if (withWorkingDirRoot) {
			info.put("workingDirRoot", workingDirRoot);
		}
		info.put("year", year);
		info.put("month", month);
		info.put("day", day);
		info.put("hour", hour);
		info.put("min", min);
		info.put("sec", sec);
		info.put("millisec", millisec);
		info.put("randomId", randomSuffix);
		
		return info;
	}

	/** Univa Grid Engine(UGE)のqsubを実行してジョブをサブミットし、結果としてUGEのjobIDを返す。
	 * 
	 * @param jobName
	 * @param scriptName
	 * @return サブミットされたJobのjobID. 何か失敗するとnullが返る。
	 * @throws IOException
	 */
	public String qsub2(String scriptName, String qsubOptions) throws IOException {
		jobId = null;

		String qsub = null;
		BashResult res = null;

		makeWorkingDir(); // 念のため。
		Bash bash = new Bash();
		bash.setWorkingDirectory(new File(getWorkingDir()));

		//qsub = "qsub -cwd -N " + jobName + " -S /bin/bash " + scriptName;

		//memory, slotの指定追加
		//qsub = "qsub " + Conf.qsubOptions + " " + jobName + " -S /bin/bash " + scriptName;

		//PqsubOptionsをOSTのパラメータに入れる。
		if (qsubOptions == null || qsubOptions.isEmpty()) {
			//業務用UGE
//			qsub = "source /home/geadmin2/UGES/uges/common/settings.sh; " + "qsub " + Conf.qsubOptions + " -S /bin/bash " + scriptName;
			//研究用UGE
			qsub = "source /home/geadmin/UGER/uger/common/settings.sh; " + "qsub " + Conf.qsubOptions + " -S /bin/bash " + scriptName;
		} else {
			//業務用UGE
//			qsub = "source /home/geadmin2/UGES/uges/common/settings.sh; " + "qsub " + Conf.qsubOptions + " " + qsubOptions + " -S /bin/bash " + scriptName;
			//研究用UGE
			qsub = "source /home/geadmin/UGER/uger/common/settings.sh; " + "qsub " + Conf.qsubOptions + " " + qsubOptions + " -S /bin/bash " + scriptName;
		}
		// 例 (本番系): qsub = "qsub -l s_vmem=64G -l mem_req=64G -pe def_slot 1 -cwd -N " + jobName + " -S /bin/bash " + scriptName
		// Note: 開発環境の qsub コマンドは幾つかのオプションを指定できない。
		System.out.println(getWorkingDir() + "\n" + qsub);


		res = bash.system(qsub);
		String stdout = res.getStdout();
		String stderr = res.getStderr();

		System.out.println("qsub output: \n" + stdout + "\n" + stderr);

		Pattern pJobID = Pattern.compile("Your job\\s+([0-9]+)\\s");

		Matcher m = pJobID.matcher(stdout);
		if (m.find()) {
			jobId = m.group(1);
		}


		return jobId;
	}

}
