import React from 'react'
import { Button } from 'react-bootstrap'
import { PAGE404 } from '../../const/imgConst'
import { ReactSVG } from 'react-svg'
import { isUserLogin } from '../../utils/loginFunction'
import Header from './../Header/Header'
import Footer from './../Footer/Footer'
import style from './PageNotFound.module.scss'

const PageNotFound = React.memo(() => {
  const isLogin = isUserLogin()
  return (
    <div className={style.pageNotFound}>
      <Header />
      <div className={style.Page404}>
        <div className={style.Page404Box}>
          <div className={style.img404}>
            <ReactSVG src={`${PAGE404}`} title="" alt="" />
          </div>
          <div className={style.txt404}>
            <h2>Sorry, Compass couldn't find the direction!</h2>
            <p>The webpage you are looking for is not here!</p>
            <div className="gap50"></div>
            <Button href={isLogin ? '/home' : '/'} variant="info" className="customButton">
              Back to Home
            </Button>
          </div>
        </div>
      </div>
      <Footer />
    </div>
  )
})

export default PageNotFound
