import { useParams } from 'react-router-dom'
import { Card, Tabs, Table, Empty, Spin, Alert } from 'antd'
import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import { useApi } from '../../hooks/useApis'

dayjs.extend(relativeTime)

export default function ApiDetail() {
  const { id } = useParams()
  const { data: api, isLoading, isError } = useApi(id)

  const versionsColumns = [
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
      render: (status) => (
        <span
          style={{
            color: status === 'published' ? 'green' : 'orange',
            fontWeight: 'bold',
          }}
        >
          {status}
        </span>
      ),
    },
    {
      title: 'Created',
      dataIndex: 'createdAt',
      key: 'createdAt',
      width: 150,
      render: (createdAt) => dayjs(createdAt).format('YYYY-MM-DD HH:mm'),
    },
    {
      title: 'Updated',
      dataIndex: 'updatedAt',
      key: 'updatedAt',
      width: 150,
      render: (updatedAt) => dayjs(updatedAt).fromNow(),
    },
  ]

  const tabItems = [
    {
      key: 'editor',
      label: 'Editor',
      children: (
        <div style={{ padding: '24px', textAlign: 'center' }}>
          <Alert
            message="Monaco Editor"
            description="Swagger/OpenAPI spec editor will be available in Sprint 3"
            type="info"
            showIcon
            style={{ marginBottom: '16px' }}
          />
          <Empty description="Editor not yet implemented" />
        </div>
      ),
    },
    {
      key: 'documentation',
      label: 'Documentation',
      children: (
        <div style={{ padding: '24px', textAlign: 'center' }}>
          <Alert
            message="Swagger UI"
            description="Interactive API documentation will be available in Sprint 4"
            type="info"
            showIcon
            style={{ marginBottom: '16px' }}
          />
          <Empty description="Documentation not yet implemented" />
        </div>
      ),
    },
    {
      key: 'versions',
      label: 'Versions',
      children: (
        <div style={{ marginTop: '16px' }}>
          {api?.versions && api.versions.length > 0 ? (
            <Table
              columns={versionsColumns}
              dataSource={api.versions}
              rowKey="id"
              pagination={false}
              size="small"
            />
          ) : (
            <Empty description="No versions available" />
          )}
        </div>
      ),
    },
  ]

  if (isLoading) {
    return (
      <div style={{ textAlign: 'center', padding: '50px' }}>
        <Spin size="large" />
      </div>
    )
  }

  if (isError) {
    return (
      <Alert
        message="Error"
        description="Failed to load API details"
        type="error"
        showIcon
      />
    )
  }

  return (
    <div>
      <Card
        title={
          <div>
            <h1 style={{ margin: 0 }}>{api?.name}</h1>
            <p style={{ margin: '8px 0 0 0', color: '#666', fontSize: '14px' }}>
              {api?.description}
            </p>
          </div>
        }
        style={{ marginBottom: '24px' }}
      >
        <div style={{ marginBottom: '16px' }}>
          <p>
            <strong>Version:</strong> {api?.version}
          </p>
          <p>
            <strong>Status:</strong>{' '}
            <span
              style={{
                color: api?.status === 'published' ? 'green' : 'orange',
                fontWeight: 'bold',
              }}
            >
              {api?.status}
            </span>
          </p>
          <p>
            <strong>Owner:</strong> {api?.owner}
          </p>
          <p>
            <strong>Last Updated:</strong> {dayjs(api?.updatedAt).format('YYYY-MM-DD HH:mm:ss')}
          </p>
        </div>

        <Tabs items={tabItems} />
      </Card>
    </div>
  )
}
