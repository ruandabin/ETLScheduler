package top.ruandb.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import top.ruandb.entity.Echart;
import top.ruandb.entity.JobLog;
import top.ruandb.entity.Serie;
import top.ruandb.entity.TaskCronJob;
import top.ruandb.service.JobLogService;
import top.ruandb.service.JobMonitorService;
import top.ruandb.service.TaskCronJobService;
import top.ruandb.utils.StringUtils;

@Controller
@RequestMapping("/jobLog")
public class JobLogController {

	@Resource
	private JobLogService jobLogService;
	
	@Resource
	private TaskCronJobService	taskCronJobService;
	
	@RequestMapping("listAll")
	@ResponseBody
	public Map<String,Object> listAllJobs(JobLog jobLog,@RequestParam(value="page",required=false)Integer page,@RequestParam(value="limit",required=false)Integer limit) {
		Map<String, Object> resultMap = new HashMap<>();
		List<JobLog> jobLogList = jobLogService.findAll(jobLog, page, limit, Sort.Direction.DESC, "id");
		Long count = jobLogService.getCount(jobLog);
		resultMap.put("code", 0);
		resultMap.put("count", count);
		resultMap.put("data", jobLogList);
		return resultMap ;
	}
	
	/**
	 * 查询一个
	 * @param id
	 * @return
	 */
	@RequestMapping("listOne")
	@ResponseBody
	public Map<String,Object> listOne(Long id){
		Map<String, Object> resultMap = new HashMap<>();
		JobLog jobLog = jobLogService.findOne(id);
		resultMap.put("jobLog", jobLog);
		resultMap.put("success", true);
		return resultMap ;
	}
	
	@RequestMapping("echart")
	@ResponseBody
	public	Map<String,Object> getEchartData(){
		Map<String, Object> resultMap = new HashMap<>();
		List<TaskCronJob> taskCronJobList = taskCronJobService.findAll();
		JobLog jobLog = new JobLog();
		
		
		List<Serie>  series = new ArrayList<>();
		List<String> legend =   new ArrayList<>();
		
		for(TaskCronJob tj : taskCronJobList) {
			
			Serie serie = new Serie();
			serie.setName(tj.getJobName());
//			List<String> serieList = new ArrayList<>();
			String[] serieList = new String[7];
			
			legend.add(tj.getJobName());
			jobLog.setJobName(tj.getJobName());
			List<JobLog> jobLogList = jobLogService.findAll(jobLog, 1, 7, Sort.Direction.DESC, "id");
			
			for(int i=0;i<jobLogList.size();i++) {
				serieList[7-i-1] = StringUtils.twoDateS(jobLogList.get(i).getStartdate(), jobLogList.get(i).getEnddate());
				
			}

			serie.setData(serieList);
			series.add(serie);
		}
		
		resultMap.put("series", series);
		resultMap.put("legend", legend);
		return resultMap ;
	}
}
