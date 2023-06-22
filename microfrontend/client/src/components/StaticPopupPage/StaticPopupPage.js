import React from 'react'
import PropTypes from 'prop-types'
import { withRouter } from 'reusable-components/dist/components/withRouter/withRouter';
import { IC_CLOSE } from 'reusable-components/dist/const/imgConst'
import style from './StaticPopupPage.module.scss'

const StaticPopupPage = ({ children, navigate, redirectUrl = '/client/overview' }) => {
  return (
    <div className={[style.staticPopupPage, 'animated fadeInUp'].join(' ')}>
      <div className={[style.modalBox].join(' ')}>
        <div
          className={style.close}
          onClick={() => {
            if (redirectUrl) {
              navigate(redirectUrl)
            } else {
              navigate('/client/overview')
            }
          }}
        >
          <img src={IC_CLOSE} title="" alt="" />
        </div>
        <div className={style.modalBoxInner}>{children}</div>
      </div>
    </div>
  )
}

StaticPopupPage.propTypes = {
  size: PropTypes.string,
  navigate: PropTypes.any,
  children: PropTypes.any,
  redirectUrl: PropTypes.any,
}

export default withRouter(StaticPopupPage)
