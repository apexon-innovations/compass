import React, { useState } from 'react'
import { Row, Col } from 'react-bootstrap'
import PropTypes from 'prop-types'
import { Tooltip as ReactTooltip } from 'react-tooltip'
import MultiSelect from '@khanacademy/react-multi-select'
import { IC_INFO } from 'reusable-components/dist/const/imgConst'
import ProductHealthOverviewSunBurst from 'reusable-components/dist/widgets/ProductHealthOverviewSunBurst/ProductHealthOverviewSunBurst'
import BracketBox from 'reusable-components/dist/components/CommonComponents/BracketBox/BracketBox'
import CommonLineChart from 'reusable-components/dist/components/CommonGraphs/CommonLineChart/CommonLineChart'
import CustomDropdown from 'reusable-components/dist/components/CommonComponents/CustomDropdown'
import CustomLegend from 'reusable-components/dist/components/CommonGraphSections/CustomLegend/CustomLegend'
import { Scrollbars } from 'react-custom-scrollbars'
import StaticPopupPage from '../../components/StaticPopupPage/StaticPopupPage'
import ProductHealthDrillDownConfig from './ProductHealthDrillDownConfig.json'
import TooltipProductHealthDrillDown from './TooltipProductHealthDrillDown'
import ProjectAData from './dataProjectAHealthDrillDown'
import ProjectBData from './dataProjectBHealthDrillDown'
import ProjectCData from './dataProjectCHealthDrillDown'
import ProjectDData from './dataProjectDHealthDrillDown'
import WidgetLegendsSolid from './WidgetLegendsSolid'
import WidgetLegendsDashed from './WidgetLegendsDashed'

// Style
import style from './ProductHealthDrillDown.module.scss'

const ProductHealthDrillDown = () => {
  const [selected, setSelected] = useState([])

  const [btnShow1, setBtnShow1] = useState()
  const [btnShow2, setBtnShow2] = useState()
  const [btnShow3, setBtnShow3] = useState()
  const [btnShow4, setBtnShow4] = useState()
  const [btnShow5, setBtnShow5] = useState()
  const [btnShow6, setBtnShow6] = useState()

  const dropDownPoints = [
    { displayName: 'Points', key: 'p1' },
    { displayName: 'Counts', key: 'c1' },
  ]
  const [pointsValue, setPointsValue] = useState(dropDownPoints[0].displayName)

  const dropDownDuration = [
    { displayName: 'Select Sprints', key: '' },
    { displayName: 'Prev 5 Sprints', key: 'sprint5' },
    { displayName: 'Prev 10 Sprints', key: 'sprint10' },
    { displayName: 'Prev 15 Sprints', key: 'sprint15' },
    { displayName: 'Prev 20 Sprints', key: 'sprint20' },
  ]
  const [durationValue, setDurationValue] = useState(dropDownDuration[0].displayName)

  const dropdownOptionsProject = [
    { label: 'Project A', value: 'p1' },
    { label: 'Project B', value: 'p2' },
    { label: 'Project C', value: 'p3' },
    { label: 'Project D', value: 'p4' },
    { label: 'Project E', value: 'p5' },
  ]

  const baseUrl = process.env.REACT_APP_API_END_POINT

  return (
    <StaticPopupPage>
      <div className="titleArea">
        <h2 className="title">
          Product Health Overview
          <div className="info" to="" data-tooltip-place="bottom" data-tip data-tooltip-id="info1">
            <img src={IC_INFO} title="" alt="" />
          </div>
          <ReactTooltip
            className="simpleTooltip"
            id="info1"
            border={true}
            borderColor="#35445f"
            arrowColor="#040a23"
          >
            <p>Product Health Overview</p>
          </ReactTooltip>
        </h2>
        <div className={[style.productHealthDropdown, 'controlSpace'].join(' ')}>
          <CustomDropdown
            alignRight={true}
            items={dropDownDuration}
            selectedOption={durationValue}
            onSelectCallback={index => {
              setDurationValue(dropDownDuration[index].displayName)
            }}
            bordered={true}
          />
          <CustomDropdown
            alignRight={true}
            items={dropDownPoints}
            selectedOption={pointsValue}
            onSelectCallback={index => {
              setPointsValue(dropDownPoints[index].displayName)
            }}
            bordered={true}
          />
        </div>
      </div>
      <Scrollbars autoHeight autoHeightMax={`calc(85vh - 100px)`}>
        <div className="graphHolderWithBg">
          <div className="controlRow">
            <div className={[style.productHealthNavTitle, 'graphNavBox'].join(' ')}>
              <div className="graphNavBox">
                <div className="caption">
                  <span>Consolidated View</span>
                  <div className="info" to="" data-tooltip-place="bottom" data-tip data-tooltip-id="info2">
                    <img src={IC_INFO} title="" alt="" />
                  </div>
                  <ReactTooltip
                    className="simpleTooltip"
                    id="info2"
                    border={true}
                    borderColor="#35445f"
                    arrowColor="#040a23"
                  >
                    <p>Story Point vs Defect Ratio</p>
                  </ReactTooltip>
                </div>
              </div>

              <div className={style.productHealthmultiselect}>
                <div
                  className={[style.productHealthmultiselect, 'transparentMultiSelect'].join(' ')}
                >
                  <MultiSelect
                    options={dropdownOptionsProject}
                    selected={selected}
                    onSelectedChanged={selected => {
                      setSelected(selected)
                    }}
                    valueRenderer={selected => {
                      return selected.length === 0 ? 'Select Project' : `(${selected.length})`
                    }}
                    overrideStrings={{
                      selectSomeItems: 'Select Project',
                      allItemsAreSelected: 'All Project are Selected',
                      selectAll: 'Select All Project',
                      search: 'Search Project',
                    }}
                  />
                </div>
              </div>
            </div>
          </div>
          <div>
            <ProductHealthOverviewSunBurst
              apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/productHealthOverview?jiraProjectIds=all`}
              bracketBox={false}
              zoomEnable={false}
            />
          </div>
        </div>
        <div className="gap50" />
        <div className="graphHolder">
          <Row className="ml-0 mr-0">
            <Col md={6} sm={6} xs={12} className="pl-0">
              <BracketBox hideShow={btnShow1 === true ? 'hide' : ''} boxTitle={'Project A'}>
                <div className="btnHideShow" onClick={() => setBtnShow1(!btnShow1)}>
                  {btnShow1 ? 'Show' : 'Hide'}
                </div>
                <div className="toggleThings">
                  <div className={style.productHealthInner}>
                    <CommonLineChart
                      allData={{
                        data: ProjectAData,
                        config: ProductHealthDrillDownConfig,
                      }}
                      customToolTip={TooltipProductHealthDrillDown}
                    />
                  </div>
                  <CustomLegend payload={WidgetLegendsSolid} />
                  <CustomLegend payload={WidgetLegendsDashed} />
                </div>
              </BracketBox>
              <BracketBox hideShow={btnShow3 === true ? 'hide' : ''} boxTitle={'Project C'}>
                <div className="btnHideShow" onClick={() => setBtnShow3(!btnShow3)}>
                  {btnShow3 ? 'Show' : 'Hide'}
                </div>
                <div className="toggleThings">
                  <div className={style.productHealthInner}>
                    <CommonLineChart
                      allData={{
                        data: ProjectCData,
                        config: ProductHealthDrillDownConfig,
                      }}
                      customToolTip={TooltipProductHealthDrillDown}
                    />
                  </div>
                  <CustomLegend payload={WidgetLegendsSolid} />
                  <CustomLegend payload={WidgetLegendsDashed} />
                </div>
              </BracketBox>
              <BracketBox hideShow={btnShow5 === true ? 'hide' : ''} boxTitle={'Project E'}>
                <div className="btnHideShow" onClick={() => setBtnShow5(!btnShow5)}>
                  {btnShow5 ? 'Show' : 'Hide'}
                </div>
                <div className="toggleThings">
                  <div className={style.productHealthInner}>
                    <CommonLineChart
                      allData={{
                        data: ProjectBData,
                        config: ProductHealthDrillDownConfig,
                      }}
                      customToolTip={TooltipProductHealthDrillDown}
                    />
                  </div>
                  <CustomLegend payload={WidgetLegendsSolid} />
                  <CustomLegend payload={WidgetLegendsDashed} />
                </div>
              </BracketBox>
            </Col>
            <Col md={6} sm={6} xs={12} className="pr-0">
              <BracketBox hideShow={btnShow2 === true ? 'hide' : ''} boxTitle={'Project B'}>
                <div className="btnHideShow" onClick={() => setBtnShow2(!btnShow2)}>
                  {btnShow2 ? 'Show' : 'Hide'}
                </div>
                <div className="toggleThings">
                  <div className={style.productHealthInner}>
                    <CommonLineChart
                      allData={{
                        data: ProjectBData,
                        config: ProductHealthDrillDownConfig,
                      }}
                      customToolTip={TooltipProductHealthDrillDown}
                    />
                  </div>
                  <CustomLegend payload={WidgetLegendsSolid} />
                  <CustomLegend payload={WidgetLegendsDashed} />
                </div>
              </BracketBox>
              <BracketBox hideShow={btnShow4 === true ? 'hide' : ''} boxTitle={'Project D'}>
                <div className="btnHideShow" onClick={() => setBtnShow4(!btnShow4)}>
                  {btnShow4 ? 'Show' : 'Hide'}
                </div>
                <div className="toggleThings">
                  <div className={style.productHealthInner}>
                    <CommonLineChart
                      allData={{
                        data: ProjectDData,
                        config: ProductHealthDrillDownConfig,
                      }}
                      customToolTip={TooltipProductHealthDrillDown}
                    />
                  </div>
                  <CustomLegend payload={WidgetLegendsSolid} />
                  <CustomLegend payload={WidgetLegendsDashed} />
                </div>
              </BracketBox>
              <BracketBox hideShow={btnShow6 === true ? 'hide' : ''} boxTitle={'Project F'}>
                <div className="btnHideShow" onClick={() => setBtnShow6(!btnShow6)}>
                  {btnShow6 ? 'Show' : 'Hide'}
                </div>
                <div className="toggleThings">
                  <div className={style.productHealthInner}>
                    <CommonLineChart
                      allData={{
                        data: ProjectAData,
                        config: ProductHealthDrillDownConfig,
                      }}
                      customToolTip={TooltipProductHealthDrillDown}
                    />
                  </div>
                  <CustomLegend payload={WidgetLegendsSolid} />
                  <CustomLegend payload={WidgetLegendsDashed} />
                </div>
              </BracketBox>
            </Col>
          </Row>
        </div>
      </Scrollbars>
    </StaticPopupPage>
  )
}

ProductHealthDrillDown.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

export default ProductHealthDrillDown
