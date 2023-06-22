import React, { Suspense, lazy } from 'react'
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'
import { NotificationContainer } from 'react-notifications'
import { Scrollbars } from 'react-custom-scrollbars'
import PageNotFound from 'reusable-components/dist/components/PageNotFound/PageNotFound'
import GraphPgBgAnimation from 'reusable-components/dist/components/GraphPgBgAnimation/GraphPgBgAnimation'
import PrivateRoutes from 'reusable-components/dist/components/PrivateRoutes/PrivateRoutes'
import Loader from 'reusable-components/dist/components/CommonComponents/Loader'
import ProjectDetailsApiCall from './components/ProjectDetailsApiCall/ProjectDetailsApiCall'
import AppCommonContainer from './AppCommonContainer'
import userRoles from 'reusable-components/dist/utils/userRoles'
const DeliveryTrendsDrillDown = lazy(() =>
  import('./screens/DeliveryTrendsDrillDown/DeliveryTrendsDrillDown'),
)
const Client = lazy(() => import('./screens/Client/Client'))
const ClientBusinessDashboard = lazy(() =>
  import('./screens/ClientBusinessDashboard/ClientBusinessDashboard'),
)
const ClientCodeHealthDrillDown = lazy(() =>
  import('./screens/ClientCodeHealthDrillDown/CodeHealthDrillDown'),
)
const VelocityDrillDown = lazy(() =>
  import('./screens/VelocityTrendsDrillDown/VelocityTrendsDrillDown'),
)
const StoryPointDefectRatio = lazy(() =>
  import('./screens/StoryPointDefectRatioDrillDown/StoryPointDefectRatio/StoryPointDefectRatio'),
)
const StoryPointDefectRatioGraphs = lazy(() =>
  import(
    './screens/StoryPointDefectRatioDrillDown/StoryPointDefectRatioGraphs/StoryPointDefectRatioGraphs'
  ),
)
const DeliveryTrendsProjectWiseDrillDown = lazy(() =>
  import('./screens/DeliveryTrendsDrillDown/DeliveryTrendsProjectWiseDrillDown'),
)
const CompletionTrendsDrillDown = lazy(() =>
  import('./screens/CompletionTrendsDrillDown/CompletionTrendsDrillDown'),
)
const DefectTrendsDrillDown = lazy(() =>
  import('./screens/DefectTrendsDrillDown/DefectTrendsDrillDown'),
)
const ProductHealthDrillDown = lazy(() =>
  import('./screens/ProductHealthDrillDown/ProductHealthDrillDown'),
)
const DefectAgeingDrillDown = lazy(() =>
  import('./screens/DefectAgeingDrillDown/DefectAgeingDrillDown'),
)

function App() {
  return (
    <React.Fragment>
      <Scrollbars autoHeight autoHeightMax={`100vh`}>
        <Router>
          <Suspense fallback={<Loader />}>
            <Routes>
              <Route path='/client/client-defect-ratio/' element={
                <PrivateRoutes
                  exact
                  requiredRoles={[userRoles.ADMIN, userRoles.CLIENT]}
                >
                  <StoryPointDefectRatio />
                </PrivateRoutes>}>
              </Route>

              <Route path='/client/client-defect-ratio/' element={
                <PrivateRoutes
                  exact
                  requiredRoles={[userRoles.ADMIN, userRoles.CLIENT]}
                >
                  <StoryPointDefectRatioGraphs />
                </PrivateRoutes>}>
              </Route>

              <Route path='/client/overview/' element={
                <PrivateRoutes
                  exact
                  requiredRoles={[userRoles.ADMIN, userRoles.CLIENT]}
                >
                  <Client />
                </PrivateRoutes>}>
              </Route>

              <Route path='/client/business-overview/' element={
                <PrivateRoutes
                  exact
                  requiredRoles={[userRoles.ADMIN, userRoles.CLIENT]}
                >
                  <ClientBusinessDashboard />
                </PrivateRoutes>}>
              </Route>

              <Route path='/client/business-health-overview/:projectId' element={
                <PrivateRoutes
                  exact
                  requiredRoles={[userRoles.ADMIN, userRoles.CLIENT]}
                >
                  <ClientCodeHealthDrillDown />
                </PrivateRoutes>}>
              </Route>

              <Route path='/client/client-delivery-trends/' element={
                <PrivateRoutes
                  exact
                  requiredRoles={[userRoles.ADMIN, userRoles.CLIENT]}
                >
                  <DeliveryTrendsDrillDown />
                </PrivateRoutes>}>
              </Route>

              <Route path='/client/client-delivery-trends/:projectId' element={
                <PrivateRoutes
                  exact
                  requiredRoles={[userRoles.ADMIN, userRoles.CLIENT]}
                >
                  <DeliveryTrendsProjectWiseDrillDown />
                </PrivateRoutes>}>
              </Route>

              <Route path='/client/client-velocity-detail/' element={
                <PrivateRoutes
                  exact
                  requiredRoles={[userRoles.ADMIN, userRoles.CLIENT]}
                >
                  <VelocityDrillDown />
                </PrivateRoutes>}>
              </Route>

              <Route path='/client/project-completion-detail/' element={
                <PrivateRoutes
                  exact
                  requiredRoles={[userRoles.ADMIN, userRoles.CLIENT]}
                >
                  <CompletionTrendsDrillDown />
                </PrivateRoutes>}>
              </Route>

              <Route path='/client/client-product-health/' element={
                <PrivateRoutes
                  exact
                  requiredRoles={[userRoles.ADMIN, userRoles.CLIENT]}
                >
                  <ProductHealthDrillDown />
                </PrivateRoutes>}>
              </Route>


              <Route path='/client/defect-trends-details/' element={
                <PrivateRoutes
                  exact
                  requiredRoles={[userRoles.ADMIN, userRoles.CLIENT]}
                >
                  <DefectTrendsDrillDown />
                </PrivateRoutes>}>
              </Route>

              <Route path='/client/defect-ageing/' element={
                <PrivateRoutes
                  exact
                  requiredRoles={[userRoles.ADMIN, userRoles.CLIENT]}
                >
                  <DefectAgeingDrillDown />
                </PrivateRoutes>}>
              </Route>

              <Route component={PageNotFound} />
            </Routes>
          </Suspense>
          <AppCommonContainer />
        </Router>
      </Scrollbars>
      <GraphPgBgAnimation />
      <NotificationContainer />
      <ProjectDetailsApiCall />
    </React.Fragment>
  )
}

export default App
