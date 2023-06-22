import React from 'react'
import { ReactSVG } from 'react-svg'
import { IMG_DASHBOARD_SWITCH } from '../../const/imgConst.js'
import style from './Loader.module.scss'

const Loader = React.memo(() => {
  return (
    <div id="loaderOverlay" className={style.loaderOverlay}>
      <div className={style.loader}>
        <ReactSVG src={`${IMG_DASHBOARD_SWITCH}`} />
      </div>
    </div>
  )
})

export default Loader
