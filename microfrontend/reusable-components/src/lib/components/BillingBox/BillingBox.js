import React, { PureComponent } from 'react'
import { Row, Col, Button } from 'react-bootstrap'
import { IC_FOLDER } from '../../const/imgConst.js'
import BracketBox from '../CommonComponents/BracketBox/BracketBox'
import { ResponsiveContainer } from 'recharts/lib/component/ResponsiveContainer'
import { Cell } from 'recharts/lib/component/Cell'
import { Sector } from 'recharts/lib/shape/Sector'
import { PieChart } from 'recharts/lib/chart/PieChart'
import { Pie } from 'recharts/lib/polar/Pie'

// Graph Data
import DataTimeSheet from './dataTimeSheet.js'

// Style
import style from './BillingBox.module.scss'

class BillingBox extends PureComponent {
  state = {
    activeIndex: [0, 1],
  }

  onPieEnter = (data, index) => {
    this.setState({
      activeIndex: index,
    })
  }

  renderMultilineLabel(props) {
    const RADIAN = Math.PI / 180
    const {
      cx,
      cy,
      midAngle,
      innerRadius,
      outerRadius,
      startAngle,
      endAngle,
      fill,
      payload,
    } = props
    const sin = Math.sin(-RADIAN * midAngle)
    const cos = Math.cos(-RADIAN * midAngle)

    const textAnchor = 'right'
    return (
      <g>
        <Sector
          cx={cx}
          cy={cy}
          innerRadius={innerRadius * 0.75}
          outerRadius={outerRadius * 0.75}
          startAngle={startAngle}
          endAngle={endAngle}
          fill={fill}
        />
        <line x1={cx} y1={cy} x2={cx + cos * 75} y2={cy + sin * 75} stroke={fill} />
        <text
          x={cx + cos * 100}
          y={cy + sin * 100}
          dx={-10}
          textAnchor={textAnchor}
          fontSize="12"
          fill={fill}
        >
          {payload.name}
        </text>
        <text
          x={cx + cos * 100}
          y={cy + sin * 100 + 0}
          dy={15}
          textAnchor={textAnchor}
          fontSize="18"
          fontWeight="700"
          fill={fill}
        >
          {payload.value}%
        </text>
      </g>
    )
  }

  render() {
    return (
      <div className={style.billingBox}>
        <BracketBox animStatus="" boxTitle={'Billing'}>
          <Row>
            <Col className={[style.anyClass, style.cols, 'border-right', 'blurAll'].join(' ')}>
              <div className={style.boxTitle}>Invoicing</div>
              <div className={style.invoiceBox}>
                <div className={style.invoiceIcon}>
                  <div className={style.icon}>
                    <img src={IC_FOLDER} title="" alt="" />
                  </div>
                  <div className={style.statusArea}>
                    <div className={style.status}>Pending</div>
                    <div className={style.blackRound}>
                      <Button>Generate</Button>
                    </div>
                  </div>
                </div>
                <div className={style.nextBilling}>
                  Next billing - April <span>(Due in 3 days)</span>
                </div>
                <div className={style.lastPayment}>Last payment received on</div>
                <div className={style.paymentOn}>May 28, 2020</div>
              </div>
            </Col>
            <Col className={[style.anyClass, style.cols, 'blurAll'].join(' ')}>
              <div className={style.boxTitle}>Timesheet</div>
              <div className={style.timesheetBox}>
                <ResponsiveContainer height={200}>
                  <PieChart width={730} height={250}>
                    <Pie
                      stroke="none"
                      activeIndex={this.state.activeIndex}
                      activeShape={this.renderMultilineLabel}
                      data={DataTimeSheet}
                      dataKey="value"
                      nameKey="name"
                      cx="50%"
                      cy="50%"
                      innerRadius={0}
                      outerRadius={80}
                      fill="#82ca9d"
                    >
                      <Cell fill="#24ff00" />
                      <Cell fill="#ffb245" />
                    </Pie>
                  </PieChart>
                </ResponsiveContainer>
              </div>
            </Col>
          </Row>
        </BracketBox>
      </div>
    )
  }
}

export default BillingBox
