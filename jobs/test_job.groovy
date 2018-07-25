def job_list = ['releaseA': [['jobA', 'labelA'], ['jobB', 'labelB']],
                'releaseB': [['jobC', 'labelC'], ['jobD', 'labelD'], ['jobE', 'labelE']]]

// in each iteration x and y will contain an element from job_list 
// e.g. in first iteration x = 'releaseA' and y = [['jobA', 'labelA'], ['jobB', 'labelB']]
job_list.each { x, y ->¨
  print y
// here we iterate through y, and in each iteration we take one element from y ([['jobA', 'labelA'], ['jobB', 'labelB']])
// and call it 'z', so in the first iteration z = ['jobA', 'labelA']
  y.each { -> z
    job(z[0]) {
      description('this job does almost nothing, just a test')
      logRotator(30, 15, -1, -1)
      label(z[1])
      parameters {
        stringParam('RELEASE', x, 'Release number')
      }
      //label('some_node_label')
      steps {
        batchFile {
          command('echo cos tam')
        }
      }
    }
  }
}
