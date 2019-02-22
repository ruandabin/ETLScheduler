package top.ruandb.service.impl;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import top.ruandb.entity.JobLog;
import top.ruandb.repository.JobLogRespository;
import top.ruandb.service.JobLogService;
import top.ruandb.utils.StringUtils;

@Service("jobLogService")
public class JobLogServiceImpl implements JobLogService{
	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private JobLogRespository jobLogRespository;

	/**
	 * 按条件分页查询
	 */
	@Override
	public List<JobLog> findAll(JobLog jobLog, Integer page, Integer pageSize, Direction direction,
			String... properties) {
		Pageable pageable = PageRequest.of(page - 1, pageSize, direction, properties);
		Page<JobLog> pageJobLog = jobLogRespository.findAll(new Specification<JobLog>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<JobLog> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {

				Predicate predicate = criteriaBuilder.conjunction();
				if (jobLog != null) {
					if (StringUtils.isNotEmpty(jobLog.getJobName())) {
						predicate.getExpressions()
								.add(criteriaBuilder.like(root.get("jobName"), "%" + jobLog.getJobName() + "%"));
					}
				}
				return predicate;
			}
		}, pageable);

		return pageJobLog.getContent();
	}

	/**
	 * 按条件统计
	 */
	@Override
	public Long getCount(JobLog jobLog) {
		Long count = jobLogRespository.count(new Specification<JobLog>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<JobLog> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				Predicate predicate = criteriaBuilder.conjunction();
				if (jobLog != null) {
					if (StringUtils.isNotEmpty(jobLog.getJobName())) {
						predicate.getExpressions()
								.add(criteriaBuilder.like(root.get("jobName"), "%" + jobLog.getJobName() + "%"));
					}
				}
				return predicate;
			}
		} );
		return count;
	}
	
	//保存或更新
	public JobLog addJobLog(JobLog jobLog) {
		JobLog jb = jobLogRespository.save(jobLog);
		return jb;
	}

	@Override
	public JobLog findOne(Long id) {
		return jobLogRespository.findById(id).get();
	}
	
	
	

}
