package top.ruandb.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.pentaho.di.core.exception.KettleException;
import org.quartz.SchedulerException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import top.ruandb.entity.TaskCronJob;
import top.ruandb.entity.XmSelect;
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
		List<TaskCronJob> taskCronJobList = taskCronJobService.findAll(taskCronJob, page, limit, Sort.Direction.ASC,"sortBz", "id");
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
	 * 手动执行
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
	
	/**
	 * 手动批量执行
	 * @param taskCronJob
	 * @return
	 */
	@RequestMapping("batchRunJob")
	@ResponseBody
	public Map<String,Object> batchRunJob(String ids ){
		Map<String, Object> resultMap = new HashMap<>();
		try {
			taskCronJobService.batchRunJob(ids);
			resultMap.put("success", true);
		} catch (SchedulerException e) {
			resultMap.put("success", false);
		}
		return resultMap;
	}
	
	/**
	 * 批量更新 crom
	 * @param ids
	 * @return
	 */
	@RequestMapping("batchEditCron")
	@ResponseBody
	public Map<String,Object> batchEditCron(String ids,String cron){
		Map<String, Object> resultMap = new HashMap<>();
		try {
			taskCronJobService.batchEditCron(ids, cron);
			resultMap.put("success", true);
		} catch (ClassNotFoundException | SchedulerException e) {
			resultMap.put("success", false);
		}
		return resultMap;
	}
	
	/**
	 * 批量更新 param
	 * @param ids
	 * @return
	 */
	@RequestMapping("batchEditParam")
	@ResponseBody
	public Map<String,Object> batchEditParam(String ids,String param){
		Map<String, Object> resultMap = new HashMap<>();
		try {
			taskCronJobService.batchEditParam(ids, param);
			resultMap.put("success", true);
		} catch (ClassNotFoundException | SchedulerException e) {
			resultMap.put("success", false);
		}
		return resultMap;
	}
	/**
	 * 	批量启停作业
	 * @param ids
	 * @param change
	 * @return
	 */
	@RequestMapping("batchST")
	@ResponseBody
	public Map<String,Object> batchST(String ids,String change){
		Map<String, Object> resultMap = new HashMap<>();
		try {
			taskCronJobService.batchST(ids, change);
			resultMap.put("success", true);
		} catch (ClassNotFoundException | SchedulerException e) {
			resultMap.put("success", false);
		}
		return resultMap;
	}
	
	/**
	 * 	初始化 调度配置
	 * @return
	 * @throws SchedulerException 
	 * @throws KettleException 
	 * @throws ClassNotFoundException 
	 */
	@RequestMapping("csh")
	@ResponseBody
	public Map<String,Object> csh() {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			taskCronJobService.csh();
			resultMap.put("success", true);
		} catch (ClassNotFoundException | KettleException | SchedulerException e) {
			e.printStackTrace();
			resultMap.put("success", false);
		}
		return resultMap;
	}
	
	/**
	 * 查询同组别依赖列表
	 * @param id
	 * @return
	 */
	@RequestMapping("findGroupPro")
	@ResponseBody
	public Map<String,Object> findGroupPro(String id){
		Map<String, Object> resultMap = new HashMap<>();
		List<TaskCronJob> taskCronJobList = taskCronJobService.findGroupPro(Long.parseLong(id));
		List<XmSelect> list = new ArrayList<>();
		//TaskCronJob tempTask = new TaskCronJob();
		

		for( TaskCronJob t : taskCronJobList) {
			XmSelect xmSelect = new XmSelect();
			xmSelect.setName(t.getNickName());
			xmSelect.setValue(t.getId().toString());
			list.add(xmSelect);
		}
		resultMap.put("data", list);
		return resultMap;
	}
	
	/**
	 * 查询同组别依赖列表
	 * @param id
	 * @return
	 */
	@RequestMapping("findGroupProAll")
	@ResponseBody
	public Map<String,Object> findGroupProAll(String jobGroup){
		Map<String, Object> resultMap = new HashMap<>();
		List<TaskCronJob> taskCronJobList = taskCronJobService.findGroupProAll(jobGroup);
		List<XmSelect> list = new ArrayList<>();

		for( TaskCronJob t : taskCronJobList) {
			XmSelect xmSelect = new XmSelect();
			xmSelect.setName(t.getNickName());
			xmSelect.setValue(t.getId().toString());
			list.add(xmSelect);
		}
		resultMap.put("data", list);
		return resultMap;
	}
	
	
}
