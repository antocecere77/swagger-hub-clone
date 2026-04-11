import { Routes, Route } from 'react-router-dom'
import MainLayout from './layouts/MainLayout'
import Dashboard from './pages/Dashboard'
import ApiList from './pages/ApiList'
import ApiDetail from './pages/ApiDetail'

function App() {
  return (
    <Routes>
      <Route element={<MainLayout />}>
        <Route path="/" element={<Dashboard />} />
        <Route path="/apis" element={<ApiList />} />
        <Route path="/apis/:id" element={<ApiDetail />} />
      </Route>
    </Routes>
  )
}

export default App
