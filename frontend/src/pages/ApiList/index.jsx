import { useState } from 'react'
import {
  Card,
  Table,
  Button,
  Input,
  Space,
  Modal,
  Form,
  Select,
  message,
  Popconfirm,
  Tag,
} from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, SearchOutlined } from '@ant-design/icons'
import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import { useApis, useCreateApi, useDeleteApi } from '../../hooks/useApis'

dayjs.extend(relativeTime)

export default function ApiList() {
  const [searchText, setSearchText] = useState('')
  const [isModalVisible, setIsModalVisible] = useState(false)
  const [form] = Form.useForm()
  const [currentPage, setCurrentPage] = useState(1)

  const { data: apisData, isLoading } = useApis({
    page: currentPage,
    limit: 10,
    search: searchText,
  })

  const createApiMutation = useCreateApi()
  const deleteApiMutation = useDeleteApi()

  const handleCreateApi = async (values) => {
    try {
      await createApiMutation.mutateAsync(values)
      message.success('API created successfully')
      setIsModalVisible(false)
      form.resetFields()
    } catch (error) {
      message.error('Failed to create API')
    }
  }

  const handleDeleteApi = async (apiId) => {
    try {
      await deleteApiMutation.mutateAsync(apiId)
      message.success('API deleted successfully')
    } catch (error) {
      message.error('Failed to delete API')
    }
  }

  const columns = [
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
      width: 180,
      sorter: (a, b) => a.name.localeCompare(b.name),
    },
    {
      title: 'Description',
      dataIndex: 'description',
      key: 'description',
      width: 250,
      ellipsis: true,
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
      width: 120,
      render: (status) => {
        const colors = {
          published: 'green',
          draft: 'orange',
          archived: 'red',
        }
        return <Tag color={colors[status] || 'blue'}>{status}</Tag>
      },
    },
    {
      title: 'Owner',
      dataIndex: 'owner',
      key: 'owner',
      width: 120,
    },
    {
      title: 'Updated',
      dataIndex: 'updatedAt',
      key: 'updatedAt',
      width: 130,
      render: (updatedAt) => dayjs(updatedAt).fromNow(),
    },
    {
      title: 'Actions',
      key: 'actions',
      width: 140,
      render: (_, record) => (
        <Space size="small">
          <Button
            type="primary"
            size="small"
            icon={<EditOutlined />}
            onClick={() => {
              // TODO: Navigate to edit page
            }}
          >
            Edit
          </Button>
          <Popconfirm
            title="Delete API"
            description="Are you sure you want to delete this API?"
            onConfirm={() => handleDeleteApi(record.id)}
            okText="Yes"
            cancelText="No"
          >
            <Button
              danger
              size="small"
              icon={<DeleteOutlined />}
              loading={deleteApiMutation.isPending}
            >
              Delete
            </Button>
          </Popconfirm>
        </Space>
      ),
    },
  ]

  return (
    <div>
      <Card
        title="API Specifications"
        extra={
          <Button
            type="primary"
            icon={<PlusOutlined />}
            onClick={() => setIsModalVisible(true)}
          >
            Create New API
          </Button>
        }
        style={{ marginBottom: '24px' }}
      >
        <Input
          placeholder="Search APIs..."
          prefix={<SearchOutlined />}
          value={searchText}
          onChange={(e) => {
            setSearchText(e.target.value)
            setCurrentPage(1)
          }}
          style={{ marginBottom: '16px' }}
        />

        <Table
          columns={columns}
          dataSource={apisData?.data || []}
          loading={isLoading}
          rowKey="id"
          pagination={{
            current: currentPage,
            pageSize: 10,
            total: apisData?.total || 0,
            onChange: setCurrentPage,
            showSizeChanger: false,
          }}
        />
      </Card>

      <Modal
        title="Create New API"
        open={isModalVisible}
        onOk={() => form.submit()}
        onCancel={() => {
          setIsModalVisible(false)
          form.resetFields()
        }}
        confirmLoading={createApiMutation.isPending}
      >
        <Form
          form={form}
          layout="vertical"
          onFinish={handleCreateApi}
        >
          <Form.Item
            label="API Name"
            name="name"
            rules={[{ required: true, message: 'Please enter API name' }]}
          >
            <Input placeholder="e.g., Pet Store API" />
          </Form.Item>

          <Form.Item
            label="Description"
            name="description"
            rules={[{ required: true, message: 'Please enter description' }]}
          >
            <Input.TextArea rows={3} placeholder="Enter API description" />
          </Form.Item>

          <Form.Item
            label="Category"
            name="category"
            rules={[{ required: true, message: 'Please select category' }]}
          >
            <Select placeholder="Select a category">
              <Select.Option value="public">Public</Select.Option>
              <Select.Option value="internal">Internal</Select.Option>
              <Select.Option value="partner">Partner</Select.Option>
            </Select>
          </Form.Item>

          <Form.Item
            label="Visibility"
            name="visibility"
            rules={[{ required: true, message: 'Please select visibility' }]}
          >
            <Select placeholder="Select visibility">
              <Select.Option value="public">Public</Select.Option>
              <Select.Option value="private">Private</Select.Option>
            </Select>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  )
}
