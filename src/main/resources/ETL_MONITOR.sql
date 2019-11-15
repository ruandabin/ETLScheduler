
CREATE TABLE [dbo].[TASK_CRON_JOB] (
  [id] bigint  IDENTITY(1,1) NOT NULL,
  [cron] varchar(30)   NULL,
  [dir] varchar(100)   NULL,
  [enabled] bit  NULL,
  [extract_style] int  NULL,
  [increase_time] varchar(50)   NULL,
  [job_class_name] varchar(100)   NULL,
  [job_description] varchar(max)   NULL,
  [job_group] varchar(20)   NULL,
  [job_name] varchar(50)   NULL,
  [job_params] varchar(100)   NULL,
  [pro_group] varchar(50)   NULL,
  [pro_job] varchar(50)   NULL,
  [job_number] varchar(50)   NULL,
  CONSTRAINT [PK_TASK_CRON_JOB] PRIMARY KEY CLUSTERED ([id])
) 
GO

CREATE TABLE [dbo].[JOB_MONITOR] (
  [id] bigint  NOT NULL,
  [errors] int  NULL,
  [jobname] varchar(50)   NULL,
  [nextDate] datetime2(7)  NULL,
  [prviousDate] datetime2(7)  NULL,
  [status] varchar(10)   NULL,
  CONSTRAINT [PK_JOB_MONITOR] PRIMARY KEY CLUSTERED ([id])
)  
GO

CREATE TABLE [dbo].[JOB_LOG] (
  [id] bigint  IDENTITY(1,1) NOT NULL,
  [enddate] datetime2(7)  NULL,
  [errors] bigint  NULL,
  [is_deal] varchar(50)   NULL,
  [jobname] varchar(50)   NULL,
  [logtext] varchar(max)   NULL,
  [startdate] datetime2(7)  NULL,
  [status] varchar(50)   NULL,
  CONSTRAINT [PK_JOB_LOG] PRIMARY KEY CLUSTERED ([id])
)  

