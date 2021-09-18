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
package jp.ac.nig.ddbj.wabi.controller.chipatlas;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import jp.ac.nig.ddbj.wabi.controller.BadRequestException;
import jp.ac.nig.ddbj.wabi.controller.InternalServerErrorException;
import jp.ac.nig.ddbj.wabi.controller.NotFoundException;
import jp.ac.nig.ddbj.wabi.controller.WabiController;
import jp.ac.nig.ddbj.wabi.job.chipatlas.ChipatlasJobInfo;
import jp.ac.nig.ddbj.wabi.job.JobIdNotInitializedException;
import jp.ac.nig.ddbj.wabi.job.WabiJobInfo;
import jp.ac.nig.ddbj.wabi.report.WabiGetErrorReport;
import jp.ac.nig.ddbj.wabi.report.WabiGetHelpReport;
import jp.ac.nig.ddbj.wabi.report.chipatlas.ChipatlasErrorReport;
import jp.ac.nig.ddbj.wabi.report.chipatlas.ChipatlasGetErrorReport;
import jp.ac.nig.ddbj.wabi.report.chipatlas.ChipatlasGetHelpReport;
import jp.ac.nig.ddbj.wabi.report.chipatlas.ChipatlasGetReportOfStatus;
import jp.ac.nig.ddbj.wabi.report.chipatlas.ChipatlasIllegalRequestReport;
import jp.ac.nig.ddbj.wabi.report.chipatlas.ChipatlasPostReport;
import jp.ac.nig.ddbj.wabi.request.WabiGetHelpRequest;
import jp.ac.nig.ddbj.wabi.request.WabiGetRequest;
import jp.ac.nig.ddbj.wabi.request.WabiGetenvRequest;
import jp.ac.nig.ddbj.wabi.request.WabiRequest;
import jp.ac.nig.ddbj.wabi.request.chipatlas.ChipatlasGetRequest;
import jp.ac.nig.ddbj.wabi.request.chipatlas.ChipatlasRequest;
import jp.ac.nig.ddbj.wabi.util.Conf;
import jp.ac.nig.ddbj.wabi.util.ConfChipatlas;
import jp.ac.nig.ddbj.wabi.validator.WabiGetenvRequestValidator;
import jp.ac.nig.ddbj.wabi.validator.chipatlas.ChipatlasPostRequestValidator;
import jp.ac.nig.ddbj.wabi.validator.chipatlas.ChipatlasGetRequestValidator;
import net.ogalab.util.linux.Bash;
import net.ogalab.util.linux.BashResult;
import net.ogalab.util.rand.RNG;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChipatlasController extends WabiController {

	public static final String WABI_BED_A_FILE = "bed_A";
	public static final String WABI_BED_B_FILE = "bed_B";
	public static final String WABI_QSUB_OPTIONS = "qsub_options";
	public static final String WABI_TSV_OUT_FILE = "wabi_result.tsv";
	public static final String WABI_HTML_OUT_FILE = "wabi_result.html";

	public static String outfilePrefix = "wabi_chipatlas_";
	
	public static Pattern patternGetenvPermittedRemoteAddr = Pattern.compile(ConfChipatlas.patternGetenvPermittedRemoteAddr);
	public static Pattern patternGetResultOfQsubPermittedRemoteAddr = Pattern.compile(ConfChipatlas.patternGetResultOfQsubPermittedRemoteAddr);
	public static Pattern patternGetStatusOfQsubPermittedRemoteAddr = Pattern.compile(ConfChipatlas.patternGetStatusOfQsubPermittedRemoteAddr);
	public static Pattern patternTestSecurityApplicationScanPagePermittedRemoteAddr = Pattern.compile(ConfChipatlas.patternTestSecurityApplicationScanPagePermittedRemoteAddr);

	@Inject
	MessageSource messageSource;

	RNG engine = null;

	@Inject
	public ChipatlasController(RNG engine) {
		super(engine);
	}

	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		/*
		 * Note: 脆弱性対策
		 * 不正なパラメータを指定された時に HTTP 500 エラーになっていたので、
		 * それを回避するために、有効なパラメータ名を列挙して指定しておく。
		 */
		binder.setAllowedFields("bedAFile", "bedBFile", "qsubOptions", "format", "result", "address",
								"typeA", "typeB", "descriptionA", "descriptionB", "title", "permTime",
								"distanceDown", "distanceUp", "genome", "antigenClass", "cellClass",
								"threshold",
								"format", "info",
								"requestId", "format", "info",
								"helpCommand", "format");
	}
	
	/**
	 * HTTP POSTメソッドで呼ばれた時の処理を行う。Job(wabi)の投入を行い、当該Job(wabi)のID
	 * (request-idと呼ぶ）を作成して返す.
	 * 
	 * @param request
	 * @return
	 */
	@Override
	public ModelAndView post(@ModelAttribute WabiRequest request,
			BindingResult errors) throws BadRequestException,
			InternalServerErrorException {
		ModelAndView result = null;
		return result;
	}
	
	
	@RequestMapping(value = "/chipatlas", method = RequestMethod.POST)
	public ModelAndView post(@Validated @ModelAttribute ChipatlasRequest request,
			BindingResult errors) throws BadRequestException,
			InternalServerErrorException, IOException {

		ModelAndView result = null;

		// ChipatlasRequestクラスのannotationによるvalidation
		if (errors.hasErrors()) {
			ChipatlasErrorReport report = new ChipatlasErrorReport(request);
			report.put("Message", "Error (POST parameters validation error)");
			System.out.println(report.get("current-time") + "["
					+ getClass() + "#get] result: (" + report + ")");
			throw new BadRequestException(report);
		}

		try {
			// 現在時刻を使って新規request-idを作成する。
			ChipatlasJobInfo jobInfo = new ChipatlasJobInfo(engine);
			jobInfo.generateRequestId();

			// 新規Job(wabi)を実行する際のワーキングディレクトリ作成
			jobInfo.makeWorkingDir();

			// Request(user), bed_A, bed_Bをワーキングディレクトリ中にファイルとしてセーブ
			jobInfo.save(USER_REQUEST_FILE, request.toJsonStr());
			jobInfo.save(WABI_BED_A_FILE, request.getBedAFile());
			jobInfo.save(WABI_BED_B_FILE, request.getBedBFile());
			jobInfo.save(WABI_QSUB_OPTIONS, request.getQsubOptions());

			// セキュリティのためにリクエスト値をチェックする。
			// 先にChipatlasRequestクラスのannotationによるvalidationを行っているため、ここでやることはない。
			// qsubのコマンドラインオプションをきちんとチェックする場合、ここでやる。
			Validator validator = new ChipatlasPostRequestValidator();
			validator.validate(request, errors);
			if (errors.hasErrors()) {
				ChipatlasIllegalRequestReport report = new ChipatlasIllegalRequestReport(request);
				jobInfo.save(ILLEGAL_ARGUMENTS_FILE, errors.getAllErrors().toString());
				throw new BadRequestException(report);
			}

			// 実行用シェルスクリプトファイル作成
			jobInfo.save(
					SHELL_SCRIPT_FILE,
					makeShellScript(
							request,
							WABI_BED_A_FILE,
							WABI_BED_B_FILE, 
							WABI_TSV_OUT_FILE,
							WABI_HTML_OUT_FILE,
							jobInfo
					)
			);

			// qsubコマンドを発行してUGEにjob(UGE)を投入
//			String jobName = "chipatlas";
//			String jobId = jobInfo.qsub(jobName, SHELL_SCRIPT_FILE);
			String jobId = jobInfo.qsub2(SHELL_SCRIPT_FILE, request.getQsubOptions());

			// jobId, jobInfoをファイルに保存
			jobInfo.save(UGE_JOB_ID_FILE, jobId + "\n"); // UGEのjob-id.
			jobInfo.save(JOB_INFO_FILE, jobInfo.getInfoAsJson(true));

			// 新規作成ジョブに関するレポートを返す
			ChipatlasPostReport report = new ChipatlasPostReport(jobInfo, request);
			result = new ModelAndView(request.getFormat(), "linked-hash-map",
					report);
		} catch (IOException e) {
			e.printStackTrace();
			LinkedHashMap<String, Object> report = null;
			try {
				report = new ChipatlasErrorReport(request);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			throw new InternalServerErrorException(report);
		}

		return result;
		
	}
	
	@Override
	public ModelAndView get(String requestId, WabiGetRequest request,
			BindingResult errors, HttpServletRequest req) throws IOException,
			BadRequestException, NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * GETメソッドで呼ばれた時の処理を行う。request-idで表されるJobの現在の状態を返す.
	 * 
	 * @param requestId
	 *            jobを特定するためのID文字列。
	 * @param request
	 *            GET入力データ (パラメータは format, imageId, info の 3種)
	 * @param req
	 *            HTTPリクエスト
	 * @param errors
	 *            エラー情報 (パラメータからモデルへの変換処理でのエラー情報を格納済み)CLUSTALW
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/chipatlas/{id}", method = RequestMethod.GET)
	public ModelAndView get(@PathVariable("id") String requestId,
			@Validated @ModelAttribute ChipatlasGetRequest request, BindingResult errors,
			HttpServletRequest req) throws IOException, BadRequestException,
			NotFoundException {

		request.setRequestId(requestId);
		
		ModelAndView result = null;
		
		// ChipatlasGetRequestクラスのannotationによるvalidation
		if (errors.hasErrors()) {
			ChipatlasGetErrorReport report = new ChipatlasGetErrorReport(request);
			report.put("Message", "Error (GET parameters validation error)");
			System.out.println(report.get("current-time") + "["
					+ getClass() + "#get] result: (" + report + ")");
			throw new BadRequestException(report);
		}

		if (request.getInfo().equals("status")) {
			try {
				ChipatlasGetReportOfStatus report = new ChipatlasGetReportOfStatus(requestId);
				result = new ModelAndView(request.getFormat(), "linked-hash-map", report);
			} catch (JobIdNotInitializedException e) {
				LinkedHashMap<String, Object> report = new ChipatlasGetErrorReport(request);
				report.put("Message", "Error (" + e + ")");
				System.out.println(report.get("current-time") + "["
						+ getClass() + "#get] status: (" + report + ")");
				throw new BadRequestException(report);
			}
		} else if (request.getInfo().equals("result")) {
			ChipatlasJobInfo jobInfo = new ChipatlasJobInfo(requestId);

			// format=html の場合
			if (request.getFormat().equals("html")) {
				if (jobInfo.existsHtmlOutFile()) {
					result = new ModelAndView("bightml", "filename",
							jobInfo.getWorkingDir() + WABI_HTML_OUT_FILE);
				} else {
					ChipatlasGetErrorReport report = new ChipatlasGetErrorReport(request);
					report.put("Message",
							"Error ( Result html file of your request id have been NOT FOUND, or still running.)");
					System.out.println(report.get("current-time") + "["
							+ getClass() + "#get] result: (" + report + ")");
					throw new NotFoundException(report);
				}

			// format=tsv の場合
			} else if (request.getFormat().equals("tsv")) {
				if (jobInfo.existsTsvOutFile()) {
					result = new ModelAndView("bigfile", "filename",
							jobInfo.getWorkingDir() + WABI_TSV_OUT_FILE);
				} else {
					ChipatlasGetErrorReport report = new ChipatlasGetErrorReport(request);
					report.put("Message",
							"Error ( Result tsv file of your request id have been NOT FOUND, or still running.)");
					System.out.println(report.get("current-time") + "["
							+ getClass() + "#get] result: (" + report + ")");
					throw new NotFoundException(report);
				}
			// formatがhtml, tsv以外、または無指定の場合
			} else {
				ChipatlasGetErrorReport report = new ChipatlasGetErrorReport(request);
				report.put("Message",
						"Error ( Appoint tsv or html in format parameter.)");
				System.out.println(report.get("current-time") + "["
						+ getClass() + "#get] result: (" + report + ")");
				throw new NotFoundException(report);
			}
			
/*			
			if (jobInfo.existsOutFile()) {
				result = new ModelAndView("bigfile", "filename", jobInfo.getWorkingDir() + WABI_OUT_FILE);
			} else {
				ChipatlasGetErrorReport report = new ChipatlasGetErrorReport(request);
				report.put("Message",
						"Error ( Results of your request id have been NOT FOUND, or still running.)");
				System.out.println(report.get("current-time") + "["
						+ getClass() + "#get] result: (" + report + ")");
				throw new NotFoundException(report);
			}
*/
		
		} else if (request.getInfo().equals("request")) {
			ChipatlasJobInfo jobInfo = new ChipatlasJobInfo(requestId);

			if (jobInfo.existsUserRequestFile()) {
				String userRequestFile = jobInfo.getWorkingDir() + USER_REQUEST_FILE;
				result = new ModelAndView("requestfile", "filename", userRequestFile);
			} else {
				ChipatlasGetErrorReport report = new ChipatlasGetErrorReport(request);
				report.put("Message",
						"Unexpected error ( Results of your request id have been NOT FOUND.)");
				System.out.println(report.get("current-time") + "["
						+ getClass() + "#get] request: (" + report + ")");
				throw new NotFoundException(report);
			}
		} else if (request.getInfo().equals("result_stdout")) {
			result = getResultOfQsubOutput(requestId, request, req, true);
		} else if (request.getInfo().equals("result_stderr")) {
			result = getResultOfQsubOutput(requestId, request, req, false);
		}

		return result;

	}

	/**
	 * GETメソッドで呼ばれた時の処理を行う。request-idで表されるJobの現在の状態を返す.
	 * 
	 * @param request
	 *            GET入力データ (パラメータは format, info の 2種)
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/chipatlas", method = RequestMethod.GET)
	public ModelAndView getenv(@ModelAttribute WabiGetenvRequest request,
			BindingResult errors, HttpServletRequest req) throws IOException,
			NotFoundException, BadRequestException {

		/*
		 * Note: セキュリティに関わるシステム情報を出力できるので、 DDBJ内部 の開発関係者だけが利用できるようにする。
		 */
		//checkRemoteAddr(req, patternGetenvPermittedRemoteAddr);

		/*
		// セキュリティのためにリクエスト値をチェックする
		Validator validator = new WabiGetenvRequestValidator();
		validator.validate(request, errors);
		if (errors.hasErrors()) {
			OkiGetErrorReport report = new OkiGetErrorReport(request);
			System.out.println(report.get("current-time") + "[" + getClass()
					+ "#getenv] validation: (" + errors.getAllErrors() + ")");
			throw new BadRequestException(report);
		}
		*/

/*		ModelAndView result = null;
		LinkedHashMap<String, String> report = new LinkedHashMap<String, String>();
		report.put("stdout", "");
		report.put("stderr", "");
		result = new ModelAndView(request.getFormat(), "linked-hash-map",
				report);
*/
		/*
		if (request.getInfo().equals("env")) {
			LinkedHashMap<String, String> report = new LinkedHashMap<String, String>();
			Bash bash = new Bash();
			BashResult res = bash.system("env | sort");
			report.put("stdout", res.getStdout());
			report.put("stderr", res.getStderr());
			result = new ModelAndView(request.getFormat(), "linked-hash-map",
					report);
		} else {
			OkiGetErrorReport report = new OkiGetErrorReport(request);
			report.put("Message",
					"Illegal arguments (info = " + request.getInfo() + ")");
			System.out.println(report.get("current-time") + "[" + getClass()
					+ "#getenv] info: (" + report + ")");
			throw new BadRequestException(report);
		}
		*/
		ModelAndView result = null;
		String message = "chipatlas";
		result = new ModelAndView("text2", "message", message);

		return result;

	}

	/**
	 * Help 情報の使い方を返します。 /blast/help/help_command の「help_command」の選択肢を返します。
	 */
	@RequestMapping(value = "/chipatlas/help", method = RequestMethod.GET)
	public ModelAndView help(@ModelAttribute WabiGetHelpRequest request,
			BindingResult errors, HttpServletRequest req) {
		return help(null, request, errors, req);
	}

	/**
	 * Help 情報を返します。
     * 例: /blast/help/list_program
	 * 
	 * help_command が省略された場合は、その使い方を返します。 /blast/help/help_command
	 * の「help_command」の選択肢を返します。
	 */
	@RequestMapping(value = "/chipatlas/help/{help_command}", method = RequestMethod.GET)
	public ModelAndView help(@PathVariable("help_command") String helpCommand,
			@ModelAttribute WabiGetHelpRequest request, BindingResult errors,
			HttpServletRequest req) {
		request.setHelpCommand(helpCommand);
		if (!"text".equals(request.getFormat())
				&& !"json".equals(request.getFormat())
				&& !"xml".equals(request.getFormat())) {
			request.setFormat("text");
		}

		boolean isPermittedRemoteAddrResultOfQsub = isPermittedRemoteAddr(req,
				patternGetResultOfQsubPermittedRemoteAddr);
		boolean isPermittedRemoteAddrGetenv = isPermittedRemoteAddr(req,
				patternGetenvPermittedRemoteAddr);
		WabiGetHelpReport report = new ChipatlasGetHelpReport(request,
				isPermittedRemoteAddrResultOfQsub, isPermittedRemoteAddrGetenv);
		return new ModelAndView(request.getFormat(), "linked-hash-map", report);
	}

	@RequestMapping(value="/oki/test/security_application_scan", method=RequestMethod.GET)
	public String showSecurityApplicationScanPage(HttpServletRequest req) throws NotFoundException {
		if (!isPermittedRemoteAddr(req, patternTestSecurityApplicationScanPagePermittedRemoteAddr)) {
			throw new NotFoundException(null);
		}
		return "test.security_application_scan";
	}

	/**
	 * CLUSTALWの計算を行うbash scriptを作成する.
	 * 
	 * <code>
	 * clustalw2 -INFILE=test2.fasta -TYPE=DNA -OUTFILE=outfile [other parameters]
	 * </code>
	 * 
	 * @param request
	 * @param wabiOutFile 
	 * @return
	 * @throws IOException
	 */
	@Override
	public String makeShellScript(
			WabiRequest request,
			String infile,
			String outfile,
			WabiJobInfo jobInfo
			) throws IOException {
		return null;
	}

	
	public String makeShellScript(
			ChipatlasRequest request,
			String bedAFile,
			String bedBFile, 
			String outTsvFile,
			String outHtmlFile,
			WabiJobInfo jobInfo
			) throws IOException {

		StringBuffer buf = new StringBuffer();
		String requestId = jobInfo.generateRequestId();

		buf.append("WORKINGDIR=" + jobInfo.getWorkingDir() + "\n");
		buf.append("MESSAGE=" + "\"The results can be viewed at:\"" + "\n");
		buf.append("SUBJECT=" + "\"[WABI] " + requestId + " is finished.\""
				+ "\n\n");

		buf.append("bedA=" + bedAFile + "\n");
		buf.append("bedB=" + bedBFile + "\n");
		buf.append("outTsv=" + outTsvFile + "\n");
		buf.append("outHtml=" + outHtmlFile + "\n");
		
		buf.append("typeA=\"" + request.getTypeA() + "\"\n");
		buf.append("typeB=\"" + request.getTypeB() + "\"\n");
		buf.append("descriptionA=\"" + request.getDescriptionA() + "\"\n");
		buf.append("descriptionB=\"" + request.getDescriptionB() + "\"\n");
		buf.append("title=\"" + request.getTitle() + "\"\n");
		buf.append("permTime=" + request.getPermTime() + "\n");
		buf.append("distanceDown=" + request.getDistanceDown() + "\n");
		buf.append("distanceUp=" + request.getDistanceUp() + "\n");
		buf.append("genome=\"" + request.getGenome() + "\"\n");
		buf.append("antigenClass=\"" + request.getAntigenClass() + "\"\n");
		buf.append("cellClass=\"" + request.getCellClass() + "\"\n");
		buf.append("threshold=" + request.getThreshold() + "\n");
		
		buf.append("sh " + Conf.bedToBoundProteinsPath);
		buf.append(" $bedA $bedB $bedL $outTsv $outHtml");
		buf.append(" \"$typeA\" \"$typeB\" \"$descriptionA\" \"$descriptionB\" \"$title\"");
		buf.append(" $permTime $distanceDown $distanceUp");
		buf.append(" \"$genome\" \"$antigenClass\" \"$cellClass\" $threshold" + "\n");

		// メール送信処理追加
		if ("mail".equals(request.getResult())) {
		buf.append("echo -e \"Request ID   " + requestId + "\\n\\n");
		buf.append("\"${MESSAGE[*]}\"" + "\\n");
		buf.append("Results\\n===========\\n");
		buf.append(" `cat " + outTsvFile + "`\" | ");
		buf.append("mail -v -s \"${SUBJECT[*]}\"");
		buf.append(" " + request.getAddress());
		buf.append(" >/dev/null 2>&1\n"); }

		buf.append("touch " + FINISHED_FILE);

		return buf.toString() + "\n";
	}

	protected ModelAndView getResultOfQsubOutput(String requestId,
			WabiGetRequest request, HttpServletRequest req, boolean isStdout)
			throws IOException, NotFoundException {
		/*
		 * Note: qsub の標準出力にはシステム情報が含まれ得るので、 接続元IPアドレス で拒否します。 システム情報の例:
		 * パスやアカウント名など。
		 */
		checkRemoteAddr(req, patternGetResultOfQsubPermittedRemoteAddr);
		WabiJobInfo jobInfo = new ChipatlasJobInfo(requestId);
		String jobName = makeJobName(jobInfo);
		String qsubOutFilename = isStdout ? jobInfo
				.getQsubStdoutFilename(jobName) : jobInfo
				.getQsubStderrFilename(jobName);
		if (null == jobName || jobInfo.existsFile(qsubOutFilename)) {
			WabiGetErrorReport report = new ChipatlasGetErrorReport(request);
			report.put(
					"Message",
					"Error ( "
							+ (isStdout ? "Stdout" : "Stderr")
							+ " of your request id have been NOT FOUND, or still running.)");
			System.out.println(report.get("current-time") + "[" + getClass()
					+ "#get] " + (isStdout ? "result_stdout" : "result_stderr")
					+ ": (" + report + ")");
			throw new NotFoundException(report);
		}
		return new ModelAndView("bigfile", "filename", qsubOutFilename);
	}

}
