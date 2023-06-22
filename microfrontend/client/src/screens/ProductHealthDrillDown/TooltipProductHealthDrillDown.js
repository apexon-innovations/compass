import React from 'react'
import style from './ProductHealthDrillDown.module.scss'

const TooltipProductHealthDrillDown = () => {
  return (
    <div className={['graphToolTip', style.ttPrductHealthOverview].join(' ')}>
      <div className={style.tooltipWrapper}>
        <h5 className={style.title}>Previous Sprint 1</h5>
        <div className={style.tooltipContent}>
          <div className={style.singleItem}>
            <h6 className={style.singleItemTitle}>
              Story <span>(290)</span>
            </h6>
            <ul className={style.singleItemList}>
              <li className={style.backlog}>
                <span className={style.label}>Backlog</span>
                <span>75</span>
              </li>
              <li className={style.completed}>
                <span className={style.label}>Completed</span>
                <span>135</span>
              </li>
              <li className={style.todo}>
                <span className={style.label}>ToDo</span>
                <span>80</span>
              </li>
            </ul>
          </div>
          <div className={style.singleItem}>
            <h6 className={style.singleItemTitle}>
              Defect <span>(190)</span>
            </h6>
            <ul className={style.singleItemList}>
              <li className={style.defectBacklog}>
                <span className={style.label}>Backlog</span>
                <span>50</span>
              </li>
              <li className={style.defectCompleted}>
                <span className={style.label}>Resolved</span>
                <span>60</span>
              </li>
              <li className={style.defectTodo}>
                <span className={style.label}>Pending</span>
                <span>80</span>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  )
}

export default TooltipProductHealthDrillDown
