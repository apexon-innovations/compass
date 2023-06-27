import React from 'react'
import PropTypes from 'prop-types'
import { Row, Col } from 'react-bootstrap'
import CircleGraph from '../../components/CommonGraphs/CircleGraph/CircleGraph'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import BracketBox from '../../components/CommonComponents/BracketBox/BracketBox'
import style from './QualityMetricsWidget.module.scss'

const responseMapFunction = response => {
  return response && response.data ? response.data : []
}

const QualityMetricsWidget = ({ apiEndPointUrl, data, callback }) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({
    apiEndPointUrl,
    data,
    callback,
    responseMapFunction,
  })

  const renderBugsVulnerabilities = (title, value, multiLineClass = '') => {
    return (
      <div className={[style.box, `${multiLineClass}`].join(' ')}>
        <div className={style.title}>{title}</div>
        <div className={style.value}>{value || 0}</div>
      </div>
    )
  }
  const renderDuplicationBox = (title, value) => {
    return (
      <div className={style.box}>
        <div className={[style.title].join(' ')}>{title}</div>
        <div className={style.value}>{value || 0}</div>
      </div>
    )
  }
  return (
    <div className={style.CodeMetricAndQualityMetrics}>
      <BracketBox animStatus="" boxTitle={'Quality Metrics'}>
        <WidgetContainer isLoading={isLoading} error={error} data={response}>
          {response && (
            <div className={style.qualityMetrics}>
              <Row className="ml-0 mr-0">
                <Col className={[style.bb, style.br].join(' ')}>
                  <div className="w-100 d-flex flex-column">
                    <div className={[style.qmLabel, 'mb-3'].join(' ')}>Overall</div>
                    <div className={[style.qmBox].join(' ')}>
                      {renderBugsVulnerabilities('Bugs', response.overall.bugs)}
                      {renderBugsVulnerabilities(
                        'Vulnerabilities',
                        response.overall.vulnerabilities,
                      )}
                    </div>
                  </div>
                </Col>
                <Col className={[style.bb].join(' ')}>
                  <div className="w-100 d-flex flex-column">
                    <div className={[style.qmLabel, 'mb-3'].join(' ')}>
                      New code since last version
                    </div>
                    <div className={[style.qmBox].join(' ')}>
                      {renderBugsVulnerabilities('New Bugs', response.new.bugs, 'twoLine')}
                      {renderBugsVulnerabilities(
                        'New Vulnerabilities',
                        response.new.vulnerabilities,
                      )}
                    </div>
                  </div>
                </Col>
              </Row>
              <Row className="ml-0 mr-0">
                <Col className={[style.br, style.bb].join(' ')}>
                  <div className="w-100 d-flex flex-column">
                    <div className={[style.qmBox].join(' ')}>
                      <div className={style.box}>
                        <div className={[style.title, 'text-left'].join(' ')}>Coverage</div>
                        <div className={style.value}>
                          <div className={style.progressGraph}>
                            <CircleGraph
                              config={{
                                rotate: '-90deg',
                                value: response.overall.codeCoverage || '0%',
                                styleLeft: '130%',
                                styleTop: '55%',
                                styleTransformValue: '0%, -50%',
                                progress: parseInt(response.overall.codeCoverage || 0),
                              }}
                            />
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </Col>
                <Col className={[style.bb].join(' ')}>
                  <div className="w-100 d-flex flex-column">
                    <div className={[style.qmBox].join(' ')}>
                      <div className={style.box}>
                        <div className={[style.title, 'text-left'].join(' ')}>New Coverage</div>
                        <div className={[style.value, 'text-left'].join(' ')}>
                          {response.new.codeCoverage ? response.new.codeCoverage : '0%'}
                        </div>
                      </div>
                    </div>
                  </div>
                </Col>
              </Row>
              <Row className="ml-0 mr-0">
                <Col className={[style.br].join(' ')}>
                  <div className="w-100 d-flex flex-column">
                    <div className={[style.qmBox].join(' ')}>
                      {renderDuplicationBox(
                        'Duplication',
                        response.overall.duplications ? response.overall.duplications : '0%',
                      )}
                      {renderDuplicationBox(
                        'Duplication blocks',
                        response.overall.duplicationBlocks,
                      )}
                    </div>
                  </div>
                </Col>
                <Col>
                  <div className="w-100 d-flex flex-column">
                    <div className={[style.qmBox].join(' ')}>
                      <div className={style.box}>
                        <div className={[style.title, 'text-left'].join(' ')}>New Duplication</div>
                        <div className={[style.value, 'text-left'].join(' ')}>
                          {response.new.duplications ? response.new.duplications : '0%'}
                        </div>
                      </div>
                    </div>
                  </div>
                </Col>
              </Row>
            </div>
          )}
        </WidgetContainer>
      </BracketBox>
    </div>
  )
}

QualityMetricsWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

export default QualityMetricsWidget
