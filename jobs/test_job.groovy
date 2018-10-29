def releases = ['10.50.11', '10.55.12', '11.50.20']

def jobs_1 = [['job1', 'param1'], ['job2', 'param2']]


// in each iteration x and y will contain an element from job_list 
// e.g. in first iteration x = 'releaseA' and y = [['jobA', 'labelA'], ['jobB', 'labelB']]

def downstream_trigger_1 = []
def downstream_trigger_2 = []

releases.each { release ->  
  jobs_1.each { j ->
    downstream_trigger_2.add(j[0])
    job(j[0]){
      description('this job does almost nothing, just a test')
      logRotator(30, 15, -1, -1)
      //label(z[1])
      parameters {
        stringParam('RELEASE', release, 'Release number')
      }
      //label('some_node_label')
      steps {
//        batchFile {
//          command('cd c:\\CTT_AUTOMATIC\\bt\\test\\vb\\log__%RELEASE%_XYZ\\\n' +
//                  conditional_line +
//                  'cd c:\\costam\\bt\\test\\vb\\')
//        }
        shell {
          command('echo $RELEASE')
        }
      }
    }
  }
  
  def release_job_name = "release_" + release
  downstream_trigger_1.add(release_job_name)
  
  job(release_job_name) {
    description('trigger all jobs for release ' + release)
    parameters {
      stringParam('RELEASE', release, 'Release number')
    }
    publishers {
      downstreamParameterized {
        trigger(downstream_trigger_2.join(", ")) {
          condition('SUCCESS')
          parameters {
            currentBuild()
          }
        }
      }
    }
  }
  
  downstream_trigger_2 = []
}

job('run_regression') {
  description('trigger all jobs')
  publishers {
    downstreamParameterized {
      trigger(downstream_trigger_1.join(", ")) {
        condition('SUCCESS')
        triggerWithNoParameters()
      }
    }
  }
}

buildPipelineView('Regression') {
      configure { view ->
        view / buildCard(class: 'au.com.centrumsystems.hudson.plugin.buildpipeline.extension.StandardBuildCard')
        view / columnHeaders(class: 'au.com.centrumsystems.hudson.plugin.buildpipeline.extension.NullColumnHeader')
        view / rowHeaders(class: 'au.com.centrumsystems.hudson.plugin.buildpipeline.extension.SimpleRowHeader')
      }
      filterBuildQueue()
      filterExecutors()
      title('Regression pipeline')
      displayedBuilds(15)
      selectedJob('run_regression')
      alwaysAllowManualTrigger()
      showPipelineParameters()
      refreshFrequency(60)
}
  
