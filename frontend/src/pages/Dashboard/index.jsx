import { Row, Col, Card, Statistic, Table, Space, Button, message } from 'antd'
import { DownloadOutlined, DeleteOutlined, EditOutlined } from '@ant-design/icons'
import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import { useApis } from '../../hooks/useApis'

dayjs.extend(relativeTime)

export default function Dashboard() {
  const { data: apis, isLoading } = useApis({ limit: 5 })

  const columns = [
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
      width: 200,
    },
    {
      title: 'Version',
      dataIndex: 'version',
      key: 'version',
      width: 100,
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      width: 100,
      render: (status) => {
        const colors = {
          published: 'green',
          draft: 'orange',
          archived: 'red',
        }
        return (
          <span
            style={{
              color: colors[status] || 'blue',
              fontWeight: 'bold',
            }}
          >
            {status}
          </span>
        )
      },
    },
    {
      title: 'Updated',
      dataIndex: 'updatedAt',
      key: 'updatedAt',
      width: 150,
      render: (updatedAt) => dayjs(updatedAt).fromNow(),
    },
  ]

  return (
    <div>
      <h1 style={{ marginBottom: '32px' }}>Welcome to SwaggerHub Clone</h1>

      <Row gutter={[16, 16]} style={{ marginBottom: '32px' }}>
        <Col xs={24} sm={8}>
          <Card>
            <Statistic
              title="Total APIs"
              value={87}
              prefix={<DownloadOutlined />}
            />
          </Card>
        </Col>
        <Col xs={24} sm={8}>
          <Card>
            <Statistic
              title="Published APIs"
              value={65}
              prefix={<DownloadOutlined />}
            />
          </Card>
        </Col>
        <Col xs={24} sm={8}>
          <Card>
            <Statistic
              title="API Versions"
              value={152}
              prefix={<DownloadOutlined />}
            />
          </Card>
        </Col>
      </Row>

      <Card title="Recent APIs" loading={isLoading}>
        <Table
          columns={columns}
          dataSource={apis?.data || []}
          loading={isLoading}
          pagination={false}
          rowKey="id"
          size="small"
        />
      </Card>
    </div>
  )
}
