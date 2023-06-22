import React, { useState } from 'react'
import PropTypes from 'prop-types'
import { withRouter } from '../../components/withRouter/withRouter'
import config from './DefectTrendAcceptanceProjectWiseConfig.json'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import CommonAreaChart from '../../components/CommonGraphs/CommonAreaChart/CommonAreaChart'
import BracketBox from '../../components/CommonComponents/BracketBox/BracketBox'
import style from './DefectTrendAcceptanceProjectWiseWidget.module.scss'
import dataMapper from '../../utils/dataMapper'
import TooltipDefectTypeAcceptance from './TooltipDefectTypeAcceptance'

const responseMapFunction = response => {
  if (response.monthData) {
    const responseData = dataMapper(response.monthData, {
      '[].month': '[].name',
      '[].actedUpon': '[].Acted Upon',
      '[].accepted': '[].Accepted',
      '[].ratio': '[].Defects',
    })

    return { data: responseData }
  }
  return false
}

const DefectTrendAcceptanceProjectWiseWidget = ({
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
                    <div className="caption">{title}</div>
                  </div>
                </div>
              )}
              <div className="toggleThings">
                <div className="clientWidgetInner">
                  <CommonAreaChart
                    allData={{
                      config: { ...config },
                      data: showHideEnable ? response.data : response.data,
                    }}
                    customToolTip={TooltipDefectTypeAcceptance}
                  />
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

DefectTrendAcceptanceProjectWiseWidget.propTypes = {
  detailsUrl: PropTypes.string,
  apiEndPointUrl: PropTypes.string,
  showHideEnable: PropTypes.bool,
  data: PropTypes.any,
  callback: PropTypes.func,
  title: PropTypes.string,
  needBracketBox: PropTypes.bool,
}

export default withRouter(DefectTrendAcceptanceProjectWiseWidget)
