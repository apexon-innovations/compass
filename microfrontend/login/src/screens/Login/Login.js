import React from 'react'
import style from './Login.module.scss'
import LogoAnimation from '../../components/LogoAnimation/LogoAnimation'
import Footer from 'reusable-components/dist/components/Footer/Footer'
import LoginForm from './Widgets/LoginForm/LoginForm'

function Login() {
  const baseUrl = process.env.REACT_APP_API_END_POINT
  return (
    <div className={style.loginPage}>
      <LogoAnimation />
      <LoginForm apiEndPointUrl={`${baseUrl}/user-service/user/login`} />
      <Footer />
    </div>
  )
}

export default Login
