def job_list = ['jobA', 'jobB', 'jobC']

job_list.each { name ->
  job(name) {
    description('this job does almost nothing, just a test')
    logRotator(30, 15, -1, -1)
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
