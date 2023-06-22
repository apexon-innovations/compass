/* eslint-disable react/prop-types */
import React, { Suspense, lazy } from 'react'
import { NotificationContainer } from 'react-notifications'
import { Scrollbars } from 'react-custom-scrollbars'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import userRoles from 'reusable-components/dist/utils/userRoles'
import AppCommonContainer from './AppCommonContainer'

import PrivateRoutes from 'reusable-components/dist/components/PrivateRoutes/PrivateRoutes'
import GraphPgBgAnimation from 'reusable-components/dist/components/GraphPgBgAnimation/GraphPgBgAnimation'
import Loader from 'reusable-components/dist/components/Loader/Loader'
import PageNotFound from "reusable-components/dist/components/PageNotFound/PageNotFound";

const Operational = lazy(() => import('./screens/Operational/Operational'))
const Overview = lazy(() => import('./screens/Overview/Overview'))
const Strategic = lazy(() => import('./screens/Strategic/Strategic'))

// const PageNotFound = lazy(() =>
//   import('reusable-components/src/lib/components/PageNotFound/PageNotFound'),
// )

const App = () => {
  return (
    <div className="App">
      <Scrollbars autoHeight autoHeightMax={`100vh`}>
        <Router>
          <Suspense fallback={<Loader />}>
            <Routes>
              <Route exact path='/pmo/operational/:projectId?' element={<PrivateRoutes
                isSearchBox={true}
                requiredRoles={[userRoles.ADMIN, userRoles.MANAGER]}
              ><Operational /></PrivateRoutes>}></Route>

              <Route exact path='/pmo/overview/:projectId?' element={<PrivateRoutes
                isSearchBox={true}
                requiredRoles={[userRoles.ADMIN, userRoles.MANAGER]}
              ><Overview /></PrivateRoutes>}></Route>

              <Route exact path='/pmo/strategic/:projectId?' element={<PrivateRoutes
                isSearchBox={true}
                requiredRoles={[userRoles.ADMIN, userRoles.MANAGER]}
              ><Strategic /></PrivateRoutes>}></Route>

              <Route element={<PageNotFound />} />
            </Routes>
          </Suspense>
          <AppCommonContainer />
          <GraphPgBgAnimation />
          <NotificationContainer />
        </Router>
      </Scrollbars>
    </div>
  )
}

export default App
