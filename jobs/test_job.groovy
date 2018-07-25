def job_list = [['jobA', 'labelA'], 
                ['jobB', 'labelB'], 
                ['jobC', 'labelC']]

// in each iteration params will contain an element from job_list 
// e.g. in first iteration params = ['jobA', 'labelA']
job_list.each { params ->
  job(params[0]) {
    description('this job does almost nothing, just a test')
    logRotator(30, 15, -1, -1)
    label(params[1])
    parameters {
      stringParam('RELEASE', 'R01.02.03.04', 'Release number')
    }
    //label('some_node_label')
    steps {
      batchFile {
        command('echo cos tam')
      }
    }
  }
}
