import { Outlet, useNavigate, useLocation } from 'react-router-dom'
import { Layout, Menu } from 'antd'
import { HomeOutlined, ApiOutlined, SearchOutlined } from '@ant-design/icons'

const { Header, Sider, Content, Footer } = Layout

export default function MainLayout() {
  const navigate = useNavigate()
  const location = useLocation()

  const menuItems = [
    {
      key: '/',
      icon: <HomeOutlined />,
      label: 'Dashboard',
      onClick: () => navigate('/'),
    },
    {
      key: '/apis',
      icon: <ApiOutlined />,
      label: 'APIs',
      onClick: () => navigate('/apis'),
    },
    {
      key: '/explore',
      icon: <SearchOutlined />,
      label: 'Explore',
      onClick: () => navigate('/explore'),
    },
  ]

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider
        breakpoint="lg"
        collapsedWidth={0}
        onBreakpoint={(broken) => {}}
        onCollapse={(collapsed) => {}}
      >
        <div
          style={{
            color: 'white',
            padding: '16px',
            fontSize: '20px',
            fontWeight: 'bold',
            textAlign: 'center',
          }}
        >
          🔷
        </div>
        <Menu
          theme="dark"
          mode="inline"
          selectedKeys={[location.pathname]}
          items={menuItems}
        />
      </Sider>
      <Layout>
        <Header
          style={{
            background: '#fff',
            paddingLeft: 24,
            boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
            display: 'flex',
            alignItems: 'center',
          }}
        >
          <h2 style={{ margin: 0, fontSize: '24px', fontWeight: 'bold' }}>
            🔷 SwaggerHub Clone
          </h2>
        </Header>
        <Content style={{ margin: '24px 16px', padding: '0 24px' }}>
          <Outlet />
        </Content>
        <Footer style={{ textAlign: 'center' }}>
          SwaggerHub Clone ©2024 - Powered by React & Ant Design
        </Footer>
      </Layout>
    </Layout>
  )
}
