package top.ruandb.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.ruandb.entity.JobMonitor;
import top.ruandb.service.JobMonitorService;

@Controller
@RequestMapping("/jobMonitor")
public class JobMonitorController {

	@Resource
	private JobMonitorService jobMonitorService;
	
	@RequestMapping("listAll")
	@ResponseBody
	public Map<String,Object> listAllJobs(JobMonitor jobMonitor,@RequestParam(value="page",required=false)Integer page,@RequestParam(value="limit",required=false)Integer limit) {
		Map<String, Object> resultMap = new HashMap<>();
		List<JobMonitor> jobLogList = jobMonitorService.findAll(jobMonitor, page, limit, Sort.Direction.DESC, "prviousDate","id");
		Long count = jobMonitorService.getCount(jobMonitor);
		resultMap.put("code", 0);
		resultMap.put("count", count);
		resultMap.put("data", jobLogList);
		return resultMap ;
	}
	
}
