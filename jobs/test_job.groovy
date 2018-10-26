def job_list = ['releaseA': [['jobA', 'labelA'], ['jobB', 'labelB']],
                'releaseB': [['jobC', 'labelC'], ['jobD', 'labelD'], ['jobE', 'labelE']]]
// in each iteration x and y will contain an element from job_list 
// e.g. in first iteration x = 'releaseA' and y = [['jobA', 'labelA'], ['jobB', 'labelB']]

def downstream_trigger = []
def conditional_line = ''
job_list.each { x, y ->
  print y
  if (x == "releaseA") {
    conditional_line = "costam A\n"
  } 
  if (x == "releaseB") {
    conditional_line = "costam B\n"
  }
// here we iterate through y, and in each iteration we take one element from y ([['jobA', 'labelA'], ['jobB', 'labelB']])
// and call it 'z', so in the first iteration z = ['jobA', 'labelA']
  y.each { z ->
    downstream_trigger.add(z[0])
    job(z[0]) {
      description('this job does almost nothing, just a test')
      logRotator(30, 15, -1, -1)
      //label(z[1])
      parameters {
        stringParam('RELEASE', x, 'Release number')
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
          command('echo ' + conditional_line)
        } 
      }
    }
  }
}

job('run_release') {
  publishers {
        downstreamParameterized {
          trigger(downstream_trigger.join(", ")) {
            condition('SUCCESS')
            parameters {
              currentBuild()
            }
          }
        }
      }
}
