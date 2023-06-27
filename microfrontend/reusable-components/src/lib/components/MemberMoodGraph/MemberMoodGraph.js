import React, { PureComponent } from 'react'
import { ResponsiveContainer } from 'recharts/lib/component/ResponsiveContainer'
import { Line } from 'recharts/lib/cartesian/Line'
import { XAxis } from 'recharts/lib/cartesian/XAxis'
import { YAxis } from 'recharts/lib/cartesian/YAxis'
import { CartesianGrid } from 'recharts/lib/cartesian/CartesianGrid'
import { ComposedChart } from 'recharts/lib/chart/ComposedChart'
import { Text } from 'recharts/lib/component/Text'
import { Tooltip } from 'recharts/lib/component/Tooltip'
import MemberMoodGraphData from './dataMemberMoodGraph.js'
import style from './MemberMoodGraph.module.scss'
import { SMILEY_1 } from '../../const/imgConst.js'

export default class MemberMoodGraph extends PureComponent {
  render() {
    return (
      <div className={style.maincontainer}>
        <div className={style.memberMoodGraph}>
          <div className={style.headding_row}>
            <div className={style.graph_title}>Sentimental Analysis</div>
            <img className={style.smiley} src={SMILEY_1} title="" alt="" />
            <div className={style.graph_rating}> 3.6</div>
          </div>
          <div className={style.posRel}>
            <ResponsiveContainer height={200}>
              <ComposedChart
                width={730}
                height={250}
                data={MemberMoodGraphData}
                margin={{ top: 10, right: 30, left: 0, bottom: 0 }}
                padding={{ bottom: 20 }}
              >
                <defs>
                  <linearGradient id="rGraphBg" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="30%" stopColor="#01ffff" stopOpacity={0} />
                    <stop offset="100%" stopColor="#01ffff" stopOpacity={0.8} />
                  </linearGradient>
                </defs>
                <XAxis
                  tick={{ fill: '#01ffff' }}
                  stroke="#01ffff"
                  allowDataOverflow="true"
                  dataKey="mood"
                  interval={0}
                  height="50"
                  textAnchor="end"
                  angle={0}
                />
                <YAxis
                  tick={{ fill: '#01ffff' }}
                  stroke="#01ffff"
                  label={
                    <Text
                      x={0}
                      y={0}
                      dx={15}
                      dy={130}
                      offset={0}
                      angle={-90}
                      fontSize={20}
                      fill="#01ffff"
                    >
                      Member
                    </Text>
                  }
                />
                <CartesianGrid vertical={false} stroke="#01ffff" fill="url(#rGraphBg)" />
                <Tooltip cursor={false} />
                <Line dataKey="member" stroke="#01ffff" dot={{ strokeWidth: 0, r: 6 }} />
              </ComposedChart>
            </ResponsiveContainer>
          </div>
        </div>
      </div>
    )
  }
}
