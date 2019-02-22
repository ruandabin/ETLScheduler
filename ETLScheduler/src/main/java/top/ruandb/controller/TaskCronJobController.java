package top.ruandb.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.quartz.SchedulerException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import top.ruandb.entity.TaskCronJob;
import top.ruandb.service.TaskCronJobService;

@Controller
@RequestMapping("/taskCronJob")
public class TaskCronJobController {

	@Resource
	private TaskCronJobService taskCronJobService ;
	
	/**
	 * 分页查找所有JOB
	 * @param taskCronJob
	 * @param page
	 * @param limit
	 * @return
	 */
	@RequestMapping("listAll")
	@ResponseBody
	public Map<String,Object> listAllJobs(TaskCronJob taskCronJob,@RequestParam(value="page",required=false)Integer page,@RequestParam(value="limit",required=false)Integer limit) {
		Map<String, Object> resultMap = new HashMap<>();
		List<TaskCronJob> taskCronJobList = taskCronJobService.findAll(taskCronJob, page, limit, Sort.Direction.DESC, "id");
		Long count = taskCronJobService.getCount(taskCronJob);
		resultMap.put("code", 0);
		resultMap.put("count", count);
		resultMap.put("data", taskCronJobList);
		return resultMap ;
	}
	
	/**
	 * 新增
	 * @param taskCronJob
	 * @return
	 * @throws SchedulerException 
	 * @throws ClassNotFoundException 
	 */
	@RequestMapping("addJob")
	@ResponseBody
	public Map<String,Object> addJob(TaskCronJob taskCronJob) throws ClassNotFoundException, SchedulerException{
		Map<String, Object> resultMap = new HashMap<>();
		taskCronJobService.addJob(taskCronJob);
		resultMap.put("success", true);
		return resultMap ;
	}
	
	/**
	 * 删除
	 * @param id
	 * @return
	 * @throws SchedulerException 
	 */
	@RequestMapping("deleteOneJob")
	@ResponseBody
	public Map<String,Object> deleteOneJob(Long id) throws SchedulerException{
		Map<String, Object> resultMap = new HashMap<>();
		taskCronJobService.deleteOneJob(id);
		resultMap.put("success", true);
		return resultMap ;
	}
	
	/**
	 * 查询一个Job
	 * @param id
	 * @return
	 */
	@RequestMapping("listOne")
	@ResponseBody
	public Map<String,Object> listOne(Long id){
		Map<String, Object> resultMap = new HashMap<>();
		TaskCronJob taskCronJob = taskCronJobService.findOne(id);
		resultMap.put("taskCronJob", taskCronJob);
		resultMap.put("success", true);
		return resultMap ;
	}
	
	/**
	 * 手动执行报表
	 * @return
	 */
	@RequestMapping("runJob")
	@ResponseBody
	public Map<String,Object> runJob(TaskCronJob taskCronJob){
		Map<String, Object> resultMap = new HashMap<>();
		try {
			taskCronJobService.runJob(taskCronJob);
			resultMap.put("success", true);
		} catch (SchedulerException e) {
			resultMap.put("success", false);
		}
		return resultMap;
	}
}
