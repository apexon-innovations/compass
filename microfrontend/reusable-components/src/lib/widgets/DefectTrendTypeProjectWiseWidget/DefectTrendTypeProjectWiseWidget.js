import React, { useState } from 'react'
import PropTypes from 'prop-types'
import { withRouter } from '../../components/withRouter/withRouter'
import config from './DefectTrendTypeProjectWiseConfig.json'
import WidgetLegendsProject from './WidgetLegendsProject.json'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import CustomLegend from '../../components/CommonGraphSections/CustomLegend/CustomLegend'
import CommonLineChart from '../../components/CommonGraphs/CommonLineChart/CommonLineChart'
import BracketBox from '../../components/CommonComponents/BracketBox/BracketBox'
import style from './DefectTrendTypeProjectWiseWidget.module.scss'
import dataMapper from '../../utils/dataMapper'

const responseMapFunction = response => {
  if (response.data) {
    const responseData = dataMapper(response.data.monthData, {
      '[].month': '[].name',
      '[].openDefects': '[].Open Defects',
      '[].closedDefects': '[].Defects Closed',
      '[].raisedDefects': '[].Defects Raised',
    })

    return { data: responseData, projectName: response.data.name }
  }
  return false
}

const DefectTrendTypeProjectWiseWidget = ({
  apiEndPointUrl,
  showHideEnable,
  data,
  callback,
  title,
  needBracketBox,
}) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({
    apiEndPointUrl,
    data,
    callback,
    responseMapFunction: responseMapFunction,
  })

  const [btnShow, setBtnShow] = useState()

  return (
    <div className={style.defectRatio}>
      <BracketBox
        hideShow={btnShow ? 'hide' : ''}
        needBracketBox={needBracketBox}
        boxTitle={title || ''}
      >
        <WidgetContainer isLoading={isLoading} error={error} data={response}>
          {response ? (
            <React.Fragment>
              {showHideEnable ? (
                <React.Fragment>
                  <div className="btnHideShow" onClick={() => setBtnShow(!btnShow)}>
                    {btnShow ? 'Show' : 'Hide'}
                  </div>
                </React.Fragment>
              ) : (
                <div className="controlRow">
                  <div className="graphNavBox">
                    <div className="caption">{response.projectName}</div>
                  </div>
                </div>
              )}
              <div className="toggleThings">
                <div className="clientWidgetInner">
                  <CommonLineChart
                    allData={{
                      config: config,
                      data: showHideEnable ? response.data : response.data,
                    }}
                  />
                  <CustomLegend payload={WidgetLegendsProject} />
                </div>
              </div>
            </React.Fragment>
          ) : (
            ''
          )}
        </WidgetContainer>
      </BracketBox>
    </div>
  )
}

DefectTrendTypeProjectWiseWidget.propTypes = {
  detailsUrl: PropTypes.string,
  apiEndPointUrl: PropTypes.string,
  showHideEnable: PropTypes.bool,
  data: PropTypes.any,
  callback: PropTypes.func,
  title: PropTypes.string,
  needBracketBox: PropTypes.bool,
}

export default withRouter(DefectTrendTypeProjectWiseWidget)
