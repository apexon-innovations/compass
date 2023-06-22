import React from 'react'
import PropTypes from 'prop-types'
import style from './SubmitterMetrics.module.scss'

const ToolTipSubmitterMetrics = props => {
  const { label, payload } = props
  if (!label || payload.length === 0 || !payload[0].payload) return null

  const data = payload[0].payload

  return (
    <div className={['graphToolTip', style.ttSubmitterMetrics].join(' ')} data-name={data['Name']}>
      <div className={style.prNumbers}>{data['Total PRs']} PRs</div>
      <div className={style.prStatus}>
        <div className={style.row}>
          <div className={[style.data, style.green].join(' ')}>{data['Merged PRs']} Merged</div>
          <div className={[style.data, style.blue].join(' ')}>{data['Open PRs']} Open</div>
        </div>
        <div className={style.row}>
          <div className={[style.data, style.red].join(' ')}>{data['Declined PRs']} Declined</div>
        </div>
      </div>
      <div className={style.detailsBox}>
        <div className={style.box}>
          <div className={style.number}>{data['Reviewer Comments']}</div>
          <div className={style.comment}>Comments / Reviews</div>
        </div>
        <div className={style.box}>
          <div className={style.number}>{data['Recommits']}</div>
          <div className={style.comment}>Recommits</div>
        </div>
      </div>
      <div className={style.avgCycleTime}>
        <div className={style.prNumbers}>{data['Average Per Cycle Time']} Average Cycle Time</div>
      </div>
    </div>
  )
}

ToolTipSubmitterMetrics.propTypes = {
  label: PropTypes.string,
  payload: PropTypes.any,
}

export default ToolTipSubmitterMetrics
