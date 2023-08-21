# XMPP Chat Client - Project Overview

## Table of Contents
1. [Introduction](#introduction)
2. [Project Objectives](#project-objectives)
3. [Development](#development)
   - [Features and Limitations](#features-and-limitations)
   - [Functionalities](#functionalities)
4. [Implementation](#implementation)
   - [User Management](#user-management)
   - [Communication](#communication)
5. [Usage](#usage)
6. [Getting Started](#getting-started)
7. [Dependencies](#dependencies)

## Introduction

The XMPP Chat Client project aims to develop a client for instant messaging that supports the XMPP (eXtensible Messaging and Presence Protocol) protocol. This protocol is open-source and designed to enable communication across different instant messaging providers. The project involves implementing essential functionalities of an XMPP client to provide a seamless and decentralized messaging experience.

## Project Objectives

- **Protocol Implementation**: Develop a client that adheres to the XMPP protocol's standards.
- **Understanding XMPP**: Gain insight into the purpose and working of the XMPP protocol.
- **Service Functionality**: Understand the core functionalities offered by the XMPP protocol.
- **Asynchronous Programming**: Grasp the basics of asynchronous programming for network development.

## Development

### Features and Limitations

The project entails creating an XMPP client that operates based on the XMPP protocol. The development will be undertaken individually. The client must encompass, at a minimum, the following functionalities:

#### User Management

1. **Register a New Account**: Allow users to register a new account on the server.
2. **Login**: Enable users to log in to their accounts.
3. **Logout**: Provide the ability to log out from an account.
4. **Account Deletion**: Allow users to delete their account from the server.

#### Communication

1. **Display Contacts**: Display a list of contacts along with their online status.
2. **Add Contact**: Allow users to add other users to their contact list.
3. **View Contact Details**: Show detailed information about a selected contact.
4. **One-on-One Communication**: Facilitate one-on-one conversations with any user/contact.
5. **Group Chat Participation**: Enable users to participate in group conversations.
6. **Set Presence Message**: Allow users to set their presence message.
7. **Send/Receive Notifications**: Provide the ability to send and receive notifications.
8. **File Transfer**: Allow users to send and receive files.

The project's user interface will be in the form of a Command-Line Interface (CLI). You are free to choose any programming language as long as it supports cross-platform compatibility. While libraries for XMPP communication can be used, third-party libraries that solve concurrent programming challenges are prohibited.

## Implementation

### User Management

The `UserManager` class provides methods to manage user-related actions, including account registration, login, contact management, and presence updates.

#### Methods

- `addPendingNotification(NotificationP notification)`: Adds a pending notification to the list.
- `getPendingNotifications()`: Retrieves the list of pending notifications.
- ...

### Communication

The `ChatManager` class handles user communication, including managing chat options and sending files.

#### Methods

- `manageChat(AbstractXMPPConnection connection)`: Manages user chat options.
- `startChatWithContact(ChatManager chatManager, String contact)`: Initiates a one-on-one chat with a contact.
- ...

## Usage

The XMPP Chat Client offers a command-line interface for users to interact with the application. Users can register, log in, manage contacts, participate in one-on-one chats and group conversations, set presence messages, and send/receive files.

## Getting Started

1. Clone the repository.
2. Install the necessary dependencies (see [Dependencies](#dependencies)).
3. Compile and run the main application.
4. Follow the on-screen prompts to interact with the XMPP Chat Client.

## Dependencies

The XMPP Chat Client relies on the following libraries:
- [Smack](https://github.com/igniterealtime/Smack): Provides all the methods that I implemented
  ```xml
  <dependency>
    <groupId>org.igniterealtime.smack</groupId>
    <artifactId>smack-tcp</artifactId>
    <version>4.2.0</version>
  </dependency>
  <dependency>
      <groupId>org.igniterealtime.smack</groupId>
      <artifactId>smack-extensions</artifactId>
      <version>4.2.0</version>
  </dependency>
  <dependency>
      <groupId>org.igniterealtime.smack</groupId>
      <artifactId>smack-im</artifactId>
      <version>4.2.0</version>
  </dependency>
  <dependency>
      <groupId>org.igniterealtime.smack</groupId>
      <artifactId>smack-java7</artifactId>
      <version>4.2.0</version>
  </dependency>
  <dependency>
      <groupId>org.igniterealtime.smack</groupId>
      <artifactId>smack-experimental</artifactId>
      <version>4.2.0</version>
  </dependency>
  ```
