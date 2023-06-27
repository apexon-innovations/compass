import React, { Suspense, lazy } from 'react'
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'
import { NotificationContainer } from 'react-notifications'
// import { PrivateRoutes, GraphPgBgAnimation, PageNotFound, Loader } from 'reusable-components'
import userRoles from 'reusable-components/dist/utils/userRoles'

import PrivateRoutes from 'reusable-components/dist/components/PrivateRoutes/PrivateRoutes'
import GraphPgBgAnimation from 'reusable-components/dist/components/GraphPgBgAnimation/GraphPgBgAnimation'
import PageNotFound from 'reusable-components/dist/components/PageNotFound/PageNotFound'
import Loader from 'reusable-components/dist/components/Loader/Loader'

const Login = lazy(() => import('./screens/Login/Login'))
const ErrorPage = lazy(() => import('./screens/ErrorPage/ErrorPage'))
const MainLandingPage = lazy(() => import('./screens/MainLandingPage/MainLandingPage'))

function App() {
  return (
    <React.Fragment>
      <Router>
        <Suspense fallback={<Loader />}>
          <Routes>
            <Route path="/" element={<Login />} exact />
            <Route path="/login" element={<Login />} exact />
            <Route path="/home" exact element={
              <PrivateRoutes
                requiredRoles={[userRoles.ADMIN, userRoles.MANAGER, userRoles.CLIENT]}>
                <MainLandingPage />
              </PrivateRoutes>} />
            <Route path="/error-page" element={<ErrorPage />} exact />
            <Route element={<PageNotFound />} />
          </Routes>
        </Suspense>
      </Router>
      <GraphPgBgAnimation />
      <NotificationContainer />
    </React.Fragment>
  )
}

export default App
